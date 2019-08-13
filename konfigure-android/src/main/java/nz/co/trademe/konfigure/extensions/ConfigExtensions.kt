package nz.co.trademe.konfigure.extensions

import android.content.Context
import nz.co.trademe.konfigure.Config
import nz.co.trademe.konfigure.ui.ConfigProvider
import nz.co.trademe.konfigure.ui.DisplayMetadata
import kotlin.properties.ReadWriteProperty

/**
 * Published extension function for simplifying the use of display metadata. Note, any consumer could implement similar extension
 * functions to ease use of config metadata.
 */
inline fun <reified T: Any> Config.config(
    group: String,
    title: String,
    description: String,
    key: String,
    defaultValue: T
): ReadWriteProperty<Any, T> = config(
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