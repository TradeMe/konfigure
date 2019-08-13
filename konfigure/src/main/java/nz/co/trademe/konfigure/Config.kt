package nz.co.trademe.konfigure

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import nz.co.trademe.konfigure.api.ConfigDelegate
import nz.co.trademe.konfigure.api.ConfigSource
import nz.co.trademe.konfigure.api.OverrideHandler
import nz.co.trademe.konfigure.internal.InMemoryOverrideHandler
import nz.co.trademe.konfigure.model.ConfigChangeEvent
import nz.co.trademe.konfigure.model.ConfigItem
import nz.co.trademe.konfigure.model.ConfigMetadata
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass

open class Config(
    @PublishedApi
    internal val configSources: List<ConfigSource>,
    @PublishedApi
    internal val overrideHandler: OverrideHandler = InMemoryOverrideHandler()
) {

    @PublishedApi
    internal val changeRelay = Channel<ConfigChangeEvent<*>>(capacity = Channel.UNLIMITED)
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
        get() = configItems.filter { overrideHandler.contains(it.key) }

    /**
     * Boolean property describing if there are any local overrides present
     */
    val hasLocalOverrides: Boolean
        get() = overrideHandler.all.isNotEmpty()

    @PublishedApi
    internal val configTypeAdapters: Set<TypeAdapter<*>> = DEFAULT_TYPE_ADAPTERS

    /**
     * Function for clearing local overrides, if an override handler is specified
     */
    fun clearLocalOverrides() {
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


    /**
     * Function for getting the value of an item - implements fallback logic
     */
    inline fun <reified T : Any> getValueOf(item: ConfigItem<T>): T {
        val mapper = resolveAdapterForType<T>()

        if (overrideHandler.contains(item.key)) {
            return overrideHandler.get(item.key).let(mapper.fromString)
        }

        return configSources
            .find { it.contains(item.key) }
            ?.get(item.key)
            ?.let(mapper.fromString) ?: item.defaultValue
    }

    /**
     * Function for setting the value of an item.
     */
    inline fun <reified T : Any> setValueOf(item: ConfigItem<T>, newValue: T) {
        // Store old value
        val oldValue: T = getValueOf(item)

        val mapper = resolveAdapterForType<T>()

        // Override value
        overrideHandler.set(item.key, newValue.let(mapper.toString))

        // Emit change
        changeRelay.offer(
            ConfigChangeEvent(
            key = item.key,
            oldValue = oldValue,
            newValue = newValue,
            metadata = item.metadata
        ))
    }

    /**
     * Main function to be used by extending classes to define various config parameters
     */
    @Suppress("MemberVisibilityCanBePrivate")
    inline fun <reified T : Any> config(
        key: String,
        defaultValue: T,
        metadata: ConfigMetadata
    ): ReadWriteProperty<Any, T> {
        val configItem = ConfigItem(key, defaultValue, metadata)
        validateAndAddItem(configItem)
        return getDelegate(configItem)
    }

    /**
     * Function for provision of a typed delegate
     */
    @PublishedApi
    internal inline fun <reified T : Any> getDelegate(configItem: ConfigItem<T>) =
        ConfigDelegate(setValue = ::setValueOf, getValue = ::getValueOf, configItem = configItem)

    /**
     * Function which performs key validation when an item is added to the config items. All keys _must_
     * be unique, else it becomes ambiguous which type the return should be when retrieving values
     * from various config sources.
     */
    @PublishedApi
    internal fun validateAndAddItem(item: ConfigItem<*>) {
        // Ensure the key of the item is unique
        val duplicateKeyEntry = configItems.asSequence().find { it.key == item.key }
        if (duplicateKeyEntry != null) {
            throw IllegalStateException("The key \"${item.key}\" already in use by item with metadata \"${duplicateKeyEntry.metadata}\" - please choose another.")
        }

        (configItems as MutableList).add(item)
    }

    /**
     * Private function for resolving a type adapter for a given type
     */
    @Suppress("UNCHECKED_CAST")
    @PublishedApi
    internal inline fun <reified T: Any> resolveAdapterForType(): TypeAdapter<T> =
        configTypeAdapters.firstOrNull { it.clazz == T::class } as? TypeAdapter<T>
            ?: throw IllegalArgumentException("Type argument ${T::class} is not supported by the configuration library.")


    inner class SubConfigApi(
        val updateConfig: (ConfigItem<*>) -> Unit
    ) {
        inline fun <reified T: Any> provideDelegate(configItem: ConfigItem<T>): ConfigDelegate<T> = getDelegate(configItem)
    }

    /**
     * Type adapter definition used mapping types from [T] to [String] synchronously
     */
    data class TypeAdapter<T: Any>(
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