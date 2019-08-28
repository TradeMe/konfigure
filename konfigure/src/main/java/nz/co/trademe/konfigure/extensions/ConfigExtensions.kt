package nz.co.trademe.konfigure.extensions

import nz.co.trademe.konfigure.api.ConfigDelegate
import nz.co.trademe.konfigure.api.ConfigRegistry
import nz.co.trademe.konfigure.model.ConfigItem
import nz.co.trademe.konfigure.model.ConfigMetadata

val ConfigRegistry.qualifiedGroup: String?
    get() {
        val parent = parent
        return when {
            parent == null -> null
            parent.qualifiedGroup == null -> group
            group == null -> parent.qualifiedGroup
            else -> "${parent.qualifiedGroup} - $group"
        }
    }

inline fun <reified T : Any> ConfigRegistry.config(
    key: String,
    defaultValue: T,
    metadata: ConfigMetadata = object: ConfigMetadata {}): ConfigDelegate<T> {
    val configItem = ConfigItem(key, defaultValue, metadata)
    return registerItem(configItem, T::class)
}