package nz.co.trademe.konfigure.api

import nz.co.trademe.konfigure.model.ConfigItem
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Delegate used for handling config gets and sets
 */
class ConfigDelegate<T>(
    private val configItem: ConfigItem<T>,
    private val setValue: (ConfigItem<T>, T) -> Unit,
    private val getValue: (ConfigItem<T>) -> T
) : ReadWriteProperty<Any, T> {
    override fun getValue(thisRef: Any, property: KProperty<*>): T = getValue(configItem)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) = setValue(configItem, value)
}