package nz.co.trademe.konfigure.api

import nz.co.trademe.konfigure.model.ConfigItem
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * Delegate used for handling config gets and sets. This is effectively internal.
 */
class ConfigDelegate<T: Any>(
    private val item: ConfigItem<T>,
    private val itemClass: KClass<T>
): ReadWriteProperty<ConfigRegistry, T> {

    override fun getValue(thisRef: ConfigRegistry, property: KProperty<*>): T =
        thisRef.getValueOf(item, itemClass)

    override fun setValue(thisRef: ConfigRegistry, property: KProperty<*>, value: T) =
        thisRef.setValueOf(item, itemClass, value)
}