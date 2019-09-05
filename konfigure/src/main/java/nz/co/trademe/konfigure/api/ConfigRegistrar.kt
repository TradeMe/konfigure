package nz.co.trademe.konfigure.api

import nz.co.trademe.konfigure.extensions.MetadataProvider
import nz.co.trademe.konfigure.internal.defaultValue
import nz.co.trademe.konfigure.model.ConfigItem
import nz.co.trademe.konfigure.model.ConfigMetadata
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * Interface which augments [ReadWriteProperty] to include delegate providing responsibilities
 */
class ConfigRegistrar<T: Any>(
    private val key: String? = null,
    private val defaultValue: T? = null,
    private val metadataProvider: MetadataProvider? = null,
    private val itemClass: KClass<T>
) {

    /**
     * Provide config delegate. This checks input information and figures out sensible
     * default values. It then instantiates the backing [ConfigItem] and registers the item with
     * the parent [ConfigRegistry]
     */
    operator fun provideDelegate(thisRef: ConfigRegistry, property: KProperty<*>): ConfigDelegate<T> {
        val key = key ?: property.name
        val default = defaultValue ?: itemClass.defaultValue
        val metadata = metadataProvider?.invoke(property) ?: object: ConfigMetadata {}

        val configItem = ConfigItem(key, default, metadata)
        thisRef.registerItem(configItem)

        // Provide this class as the delegate
        return object: ConfigDelegate<T> {
            override fun getValue(thisRef: ConfigRegistry, property: KProperty<*>): T =
                thisRef.getValueOf(configItem, itemClass)

            override fun setValue(thisRef: ConfigRegistry, property: KProperty<*>, value: T) =
                thisRef.setValueOf(configItem, itemClass, value)
        }
    }
}