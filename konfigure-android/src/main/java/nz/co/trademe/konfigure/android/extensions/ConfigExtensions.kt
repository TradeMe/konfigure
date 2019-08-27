package nz.co.trademe.konfigure.android.extensions

import android.content.Context
import nz.co.trademe.konfigure.Config
import nz.co.trademe.konfigure.api.ConfigDelegate
import nz.co.trademe.konfigure.api.ConfigRegistry
import nz.co.trademe.konfigure.android.ui.ConfigProvider
import nz.co.trademe.konfigure.android.ui.DisplayMetadata
import nz.co.trademe.konfigure.extensions.config
import nz.co.trademe.konfigure.extensions.qualifiedGroup

/**
 * Published extension function for simplifying the use of display metadata. Note, any consumer could implement similar extension
 * functions to ease use of [DisplayMetadata].
 *
 * @param group The group to assign this config item to
 * @param title The title to display in the override UI
 * @param description The description to display in the override UI
 * @param key The Config Key
 * @param defaultValue The default value of this config item
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
 * Extension property for getting this applications config instance from the Application context
 */
val Context.applicationConfig: Config
    get() = (this.applicationContext as? ConfigProvider)?.config
        ?: throw IllegalStateException("Application must implement config provider")