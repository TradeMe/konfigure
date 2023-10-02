package nz.co.trademe.konfigure

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import nz.co.trademe.konfigure.api.ConfigRegistry
import nz.co.trademe.konfigure.api.ConfigSource
import nz.co.trademe.konfigure.api.OverrideHandler
import nz.co.trademe.konfigure.extensions.configDateFormat
import nz.co.trademe.konfigure.internal.InMemoryOverrideHandler
import nz.co.trademe.konfigure.model.ConfigChangeEvent
import nz.co.trademe.konfigure.model.ConfigItem
import java.util.Date
import kotlin.reflect.KClass

/**
 * The main class for handling Config registration. This validates key uniqueness, registers items,
 * and provides the property delegates which handle value lookup.
 *
 * To use Konfigure, you must first create a class which extends from [Config], and provides implementations
 * for [ConfigSource]s, as well as optionally an [OverrideHandler]. If an [OverrideHandler] isn't provided,
 * overridden values will be persisted in memory, at the same scope as your [Config] instance.
 *
 * Example of usage:
 *
 * ```
 * class ApplicationConfig: Config(
 *     configSources = listOf(RemoteSource()),
 *     overrideHandler = PersistentOverrideHandler()
 * ) {
 *
 *     // Register a top-level config item
 *     val testConfig: String by config(
 *         key = "test_config",
 *         defaultValue = "default")
 *
 *     // Register a subconfig group
 *     val subconfig = GroupedSubconfig(parent = this)
 * }
 * ```
 *
 * @param configSources The config sources to draw values from, with priority given to sources closest to the start of the list.
 * @param overrideHandler The override handler to store overridden values.
 *
 */
open class Config(
    private val configSources: List<ConfigSource>,
    private val overrideHandler: OverrideHandler = InMemoryOverrideHandler()
) : ConfigRegistry {

    /**
     * Config changes are modeled as a hot stream of [ConfigChangeEvent]s
     */
    private val _changes = MutableStateFlow<ConfigChangeEvent<*>?>(null)
    val changes: Flow<ConfigChangeEvent<*>> = _changes.filterNotNull()

    /**
     * List of all config items binded to this config instance
     */
    val configItems: List<ConfigItem<*>> = ArrayList()

    /**
     * List of all modified config items binded to this config instance
     */
    val modifiedItems: List<ConfigItem<*>>
        get() = configItems.filter { overrideHandler.all.contains(it.key) }

    /**
     * Boolean property describing if there are any local overrides present.
     *
     * Filters overrides by config item key, to ensure [hasLocalOverrides] doesn't
     * present as true in cases where overrides contain values not associated with the current
     * set of Config keys.
     */
    val hasLocalOverrides: Boolean
        get() = overrideHandler.all
            .filter { entry -> configItems.any { it.key == entry.key } }
            .isNotEmpty()

    override fun <T : Any> registerItem(item: ConfigItem<T>) {
        // Ensure the key of the item is unique
        val duplicateKeyEntry = configItems.asSequence().find { it.key == item.key }

        check(duplicateKeyEntry == null) {
            "The key \"${item.key}\" already in use by item with metadata \"${duplicateKeyEntry?.metadata}\" - please choose another."
        }

        (configItems as MutableList).add(item)
    }

    override fun <T : Any> getValueOf(item: ConfigItem<T>, itemClass: KClass<T>): T {
        val mapper = resolveAdapterForClass(itemClass)

        val overriddenValue = overrideHandler.all[item.key]?.let(mapper.fromString)

        val configSourceValue = configSources
            .mapNotNull { it.all[item.key] }
            .firstOrNull()
            ?.let(mapper.fromString)

        return overriddenValue ?: configSourceValue ?: item.defaultValue
    }

    override fun <T : Any> setValueOf(item: ConfigItem<T>, itemClass: KClass<T>, newValue: T) {
        // Store old value
        val oldValue: T = getValueOf(item, itemClass)

        val mapper = resolveAdapterForClass(itemClass)

        // Override value
        overrideHandler.set(item.key, newValue.let(mapper.toString))

        // Emit change
        _changes.tryEmit(
            ConfigChangeEvent(
                key = item.key,
                oldValue = oldValue,
                newValue = newValue,
                metadata = item.metadata
            )
        )
    }

    /**
     * Function for clearing local overrides, if an override handler is specified
     */
    fun clearOverrides() {
        // Collate all changes
        val changeEvents = overrideHandler.all
            .mapNotNull { entry ->
                val item = configItems.firstOrNull { it.key == entry.key }

                // If the item is null, the override key no longer exists in config. It may have been removed
                // between app versions. As such, we should clear the override fo this key as it's now redundant.
                if (item == null) {
                    overrideHandler.clear(entry.key)
                    null
                } else {
                    item to entry.value
                }
            }
            .map { (item, currentValue) ->
                ConfigChangeEvent(
                    key = item.key,
                    oldValue = currentValue,
                    newValue = item.defaultValue.toString(),
                    metadata = item.metadata
                )
            }

        // Clear items
        overrideHandler.all
            .map { it.key }
            .forEach(overrideHandler::clear)

        // Publish events
        changeEvents.forEach {
            _changes.tryEmit(it)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> resolveAdapterForClass(clazz: KClass<T>): TypeAdapter<T> =
        DEFAULT_TYPE_ADAPTERS.firstOrNull { it.clazz == clazz } as? TypeAdapter<T>
            ?: throw NoSuchElementException("Type argument $clazz is not supported by konfigure.")

    /**
     * Type adapter definition used mapping types from [T] to [String] synchronously
     */
    internal data class TypeAdapter<T : Any>(
        val clazz: KClass<T>,
        val toString: (T) -> String,
        val fromString: (String) -> T
    )

    companion object {

        private val DEFAULT_TYPE_ADAPTERS = hashSetOf(
            TypeAdapter(
                clazz = Int::class,
                fromString = String::toInt,
                toString = Int::toString
            ),
            TypeAdapter(
                clazz = Long::class,
                fromString = String::toLong,
                toString = Long::toString
            ),
            TypeAdapter(
                clazz = Float::class,
                fromString = String::toFloat,
                toString = Float::toString
            ),
            TypeAdapter(
                clazz = Double::class,
                fromString = String::toDouble,
                toString = Double::toString
            ),
            TypeAdapter(
                clazz = Boolean::class,
                fromString = String::toBoolean,
                toString = Boolean::toString
            ),
            TypeAdapter(
                clazz = String::class,
                fromString = { it },
                toString = { it }
            ),
            TypeAdapter(
                clazz = Date::class,
                fromString = { configDateFormat.parse(it) },
                toString = { configDateFormat.format(it) }
            )
        )
    }
}
