package nz.co.trademe.konfigure.api

import nz.co.trademe.konfigure.model.ConfigItem
import kotlin.reflect.KClass

/**
 * Interface outlining the roles of a [ConfigRegistry], i.e an object which handles registration
 * of config items. These optionally have parent config registries and groups, to handle the
 * case where nesting of config is required.
 */
interface ConfigRegistry {

    val parent: ConfigRegistry?
        get() = null

    val group: String?
        get() = null

    fun <T: Any> registerItem(item: ConfigItem<T>, itemClass: KClass<T>): ConfigDelegate<T>

    fun <T: Any> getValueOf(item: ConfigItem<T>, itemClass: KClass<T>): T

    fun <T: Any> setValueOf(item: ConfigItem<T>, itemClass: KClass<T>, newValue: T)
}