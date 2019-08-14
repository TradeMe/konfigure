package nz.co.trademe.konfigure.util

import nz.co.trademe.konfigure.Config
import nz.co.trademe.konfigure.ui.DisplayMetadata
import kotlin.properties.ReadWriteProperty

/**
 * Custom metadata which expands on what's required to display and adds a flag around requiring restarts.
 */
interface RestartMetadata: DisplayMetadata {
    val requiresRestart: Boolean
}

/**
 * Custom config Extension function for adding restart support.
 */
inline fun <reified T: Any> Config.config(
    group: String,
    title: String,
    description: String,
    key: String,
    defaultValue: T,
    requiresRestart: Boolean = false
): ReadWriteProperty<Any, T> = config(
    key = key,
    defaultValue = defaultValue,
    metadata = object: RestartMetadata {
        override val title: String = title
        override val group: String = group
        override val description: String = description
        override val requiresRestart: Boolean = requiresRestart
    }
)
