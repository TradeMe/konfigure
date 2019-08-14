package nz.co.trademe.konfigure

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import nz.co.trademe.konfigure.api.ConfigDelegate
import nz.co.trademe.konfigure.api.ConfigRegistry
import nz.co.trademe.konfigure.api.ConfigSource
import nz.co.trademe.konfigure.api.OverrideHandler
import nz.co.trademe.konfigure.internal.InMemoryOverrideHandler
import nz.co.trademe.konfigure.model.ConfigChangeEvent
import nz.co.trademe.konfigure.model.ConfigItem
import kotlin.reflect.KClass

open class Config(
    private val configSources: List<ConfigSource>,
    private val overrideHandler: OverrideHandler = InMemoryOverrideHandler()
): ConfigRegistry {

    private val changeRelay = Channel<ConfigChangeEvent<*>>(capacity = Channel.UNLIMITED)

    val changes: Flow<ConfigChangeEvent<*>> = flow {
        for (event in changeRelay) emit(event)
    }

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
     * Boolean property describing if there are any local overrides present
     */
    val hasLocalOverrides: Boolean
        get() = overrideHandler.all.isNotEmpty()

    override fun <T : Any> registerItem(item: ConfigItem<T>, itemClass: KClass<T>): ConfigDelegate<T> {
        validateAndAddItem(item)
        return ConfigDelegate(item, itemClass)
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
        changeRelay.offer(
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
            .map { entry ->
                configItems.first { it.key == entry.key } to entry.value
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
            changeRelay.offer(it)
        }
    }

    private fun validateAndAddItem(item: ConfigItem<*>) {
        // Ensure the key of the item is unique
        val duplicateKeyEntry = configItems.asSequence().find { it.key == item.key }
        if (duplicateKeyEntry != null) {
            throw IllegalStateException("The key \"${item.key}\" already in use by item with metadata \"${duplicateKeyEntry.metadata}\" - please choose another.")
        }

        (configItems as MutableList).add(item)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T: Any> resolveAdapterForClass(clazz: KClass<T>): TypeAdapter<T> =
        DEFAULT_TYPE_ADAPTERS.firstOrNull { it.clazz == clazz } as? TypeAdapter<T>
            ?: throw NoSuchElementException("Type argument $clazz is not supported by konfigure.")

    /**
     * Type adapter definition used mapping types from [T] to [String] synchronously
     */
    data class TypeAdapter<T : Any>(
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
            ))
    }
}