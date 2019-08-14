package nz.co.trademe.konfigure.api

import nz.co.trademe.konfigure.model.ConfigItem
import kotlin.reflect.KClass

interface ConfigRegistry {

    val parent: ConfigRegistry?
        get() = null

    val group: String?
        get() = null

    fun <T: Any> registerItem(item: ConfigItem<T>, itemClass: KClass<T>): ConfigDelegate<T>

    fun <T: Any> getValueOf(item: ConfigItem<T>, itemClass: KClass<T>): T

    fun <T: Any> setValueOf(item: ConfigItem<T>, itemClass: KClass<T>, newValue: T)
}