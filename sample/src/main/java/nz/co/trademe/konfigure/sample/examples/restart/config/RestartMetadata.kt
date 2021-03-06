package nz.co.trademe.konfigure.sample.examples.restart.config

import nz.co.trademe.konfigure.android.ui.DisplayMetadata
import nz.co.trademe.konfigure.api.ConfigRegistry
import nz.co.trademe.konfigure.extensions.config
import nz.co.trademe.konfigure.extensions.qualifiedGroup
import nz.co.trademe.konfigure.api.ConfigRegistrar

/**
 * Custom metadata which expands on what's required to display and adds a flag around requiring restarts.
 */
interface RestartMetadata {
    val requiresRestart: Boolean
}

inline fun <reified T : Any> ConfigRegistry.config(
    key: String,
    defaultValue: T,
    title: String,
    description: String,
    requiresRestart: Boolean = false): ConfigRegistrar<T> = config(
    key = key,
    defaultValue = defaultValue,
    metadata = object: RestartMetadata, DisplayMetadata {
        override val requiresRestart: Boolean = requiresRestart
        override val title: String = title
        override val description: String = description
        override val group: String = qualifiedGroup ?: ""
    }
)
