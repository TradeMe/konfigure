package nz.co.trademe.konfigure.android.ui

import nz.co.trademe.konfigure.model.ConfigMetadata

interface DisplayMetadata: ConfigMetadata {
    val title: String
    val group: String
    val description: String
}