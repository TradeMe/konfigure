package nz.co.trademe.konfigure.extensions

import android.content.Context
import nz.co.trademe.konfigure.Config
import nz.co.trademe.konfigure.api.ConfigDelegate
import nz.co.trademe.konfigure.api.ConfigRegistry
import nz.co.trademe.konfigure.ui.ConfigProvider
import nz.co.trademe.konfigure.ui.DisplayMetadata

/**
 * Published extension function for simplifying the use of display metadata. Note, any consumer could implement similar extension
 * functions to ease use of config metadata.
 */
inline fun <reified T: Any> ConfigRegistry.config(
    group: String = qualifiedGroup ?: "Ungrouped",
    title: String,
    description: String,
    key: String,
    defaultValue: T
): ConfigDelegate<T> = config(
    key = key,
    defaultValue = defaultValue,
    metadata = object: DisplayMetadata {
        override val title: String = title
        override val group: String = group
        override val description: String = description
    }
)

/**
 * Extension value for getting this applications config instance from the Application context
 */
val Context.applicationConfig: Config
    get() = (this.applicationContext as? ConfigProvider)?.config
        ?: throw IllegalStateException("Application must implement config provider")