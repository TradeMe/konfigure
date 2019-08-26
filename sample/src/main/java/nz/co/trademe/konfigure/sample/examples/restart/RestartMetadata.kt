package nz.co.trademe.konfigure.sample.examples.restart

import nz.co.trademe.konfigure.android.ui.DisplayMetadata

/**
 * Custom metadata which expands on what's required to display and adds a flag around requiring restarts.
 */
interface RestartMetadata : DisplayMetadata {
    val requiresRestart: Boolean
    override val title: String
    override val description: String
    override val group: String
}
