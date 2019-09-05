package nz.co.trademe.konfigure.sample.examples.restart

import nz.co.trademe.konfigure.android.ui.DisplayMetadata
import nz.co.trademe.konfigure.api.ConfigRegistry
import nz.co.trademe.konfigure.extensions.config
import nz.co.trademe.konfigure.extensions.qualifiedGroup
import nz.co.trademe.konfigure.api.ConfigRegistrar

/**
 * Custom metadata which expands on what's required to display and adds a flag around requiring restarts.
 */
interface RestartMetadata : DisplayMetadata {
    val requiresRestart: Boolean
    override val title: String
    override val description: String
    override val group: String
}

inline fun <reified T : Any> ConfigRegistry.config(
    key: String,
    defaultValue: T,
    title: String,
    description: String,
    requiresRestart: Boolean = false): ConfigRegistrar<T> = config(
    key = key,
    defaultValue = defaultValue,
    metadata = object: RestartMetadata {
        override val requiresRestart: Boolean = requiresRestart
        override val title: String = title
        override val description: String = description
        override val group: String = qualifiedGroup ?: ""
    }
)
