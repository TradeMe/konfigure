package nz.co.trademe.konfigure.android.ui

import nz.co.trademe.konfigure.model.ConfigMetadata

/**
 * Custom metadata implementation which adds UI-related properties.
 */
interface DisplayMetadata: ConfigMetadata {
    val title: String
    val group: String
    val description: String
}