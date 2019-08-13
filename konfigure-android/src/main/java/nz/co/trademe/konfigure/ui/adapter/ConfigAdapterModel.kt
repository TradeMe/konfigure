package nz.co.trademe.konfigure.ui.adapter

import nz.co.trademe.konfigure.ui.DisplayMetadata
import nz.co.trademe.konfigure.model.ConfigItem

/**
 * Model used for defining items displayable via the adapter.
 */
internal sealed class ConfigAdapterModel(val key: String? = null) {

    object Divider : ConfigAdapterModel()

    object ResetToDefaultFooter : ConfigAdapterModel()

    data class GroupHeader(val name: String) : ConfigAdapterModel(key = name)

    data class StringConfig(
        val item: ConfigItem<String>,
        val value: String,
        val isModified: Boolean,
        val metadata: DisplayMetadata
    ) : ConfigAdapterModel(key = item.key)

    data class LongConfig(
        val item: ConfigItem<Long>,
        val value: Long,
        val isModified: Boolean,
        val metadata: DisplayMetadata
    ) : ConfigAdapterModel(key = item.key)

    data class BooleanConfig(
        val item: ConfigItem<Boolean>,
        val value: Boolean,
        val isModified: Boolean,
        val metadata: DisplayMetadata
    ) : ConfigAdapterModel(key = item.key)
}