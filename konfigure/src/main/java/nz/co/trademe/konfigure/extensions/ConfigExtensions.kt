package nz.co.trademe.konfigure.extensions

import nz.co.trademe.konfigure.api.ConfigRegistry
import nz.co.trademe.konfigure.api.ConfigRegistrar
import nz.co.trademe.konfigure.model.ConfigMetadata
import kotlin.reflect.KProperty

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

typealias MetadataProvider = (KProperty<*>) -> ConfigMetadata

@Suppress("unused") // Unused suppression as it's used for extension function scoping
inline fun <reified T : Any> ConfigRegistry.config(
    key: String? = null,
    defaultValue: T? = null,
    metadata: ConfigMetadata? = null,
    noinline metadataProvider: MetadataProvider? = when (metadata) {
        null -> null
        else -> {{ metadata }}
    }
): ConfigRegistrar<T> =
    ConfigRegistrar(
        key = key,
        defaultValue = defaultValue,
        metadataProvider = metadataProvider,
        itemClass = T::class
    )