package nz.co.trademe.konfigure.android.ui

import nz.co.trademe.konfigure.api.ConfigRegistry
import nz.co.trademe.konfigure.extensions.qualifiedGroup
import nz.co.trademe.konfigure.model.ConfigMetadata
import kotlin.reflect.KProperty

/**
 * Custom metadata implementation which adds UI-related properties.
 */
interface DisplayMetadata: ConfigMetadata {
    val title: String
    val group: String
    val description: String

    companion object {
        fun from(
            property: KProperty<*>,
            registry: ConfigRegistry,
            group: String? = null,
            title: String? = null,
            description: String? = null
        ): DisplayMetadata = object: DisplayMetadata {
            override val title: String =
                title ?: property.name

            override val group: String =
                group ?: registry.qualifiedGroup ?: "Ungrouped"

            override val description: String =
                description ?: "Applies changes to the ${property.name} property"
        }
    }
}