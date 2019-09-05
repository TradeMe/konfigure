package nz.co.trademe.konfigure.sample.examples.filtering.config

import nz.co.trademe.konfigure.android.ui.DisplayMetadata
import nz.co.trademe.konfigure.api.ConfigRegistrar
import nz.co.trademe.konfigure.api.ConfigRegistry
import nz.co.trademe.konfigure.extensions.config

/**
 * Marker interface to add information to our metadata object
 */
interface UserVisibleMetadata

/**
 * Extension function for aiding implementaion of user visible config items.
 * We make some opinionated decisions around this, forcing developers to supply a title and
 * description for UX reasons.
 */
inline fun <reified T : Any> ConfigRegistry.userVisibleConfig(
    key: String,
    title: String,
    description: String,
    defaultValue: T? = null
): ConfigRegistrar<T> = config(
    key = key,
    defaultValue = defaultValue,
    metadataProvider = { property ->
        object: UserVisibleMetadata, DisplayMetadata by DisplayMetadata.from(
            property,
            this@userVisibleConfig,
            title = title,
            description = description) {}
    }
)