package nz.co.trademe.konfigure.android.ui.adapter

import nz.co.trademe.konfigure.android.ui.DisplayMetadata
import nz.co.trademe.konfigure.model.ConfigItem
import java.util.Date

private const val RESET_FOOTER_KEY = "reset_to_default_key"

/**
 * Model used for defining items displayable via the adapter.
 */
internal sealed class ConfigAdapterModel(val key: String? = null) {

    object Divider : ConfigAdapterModel()

    object ResetToDefaultFooter : ConfigAdapterModel(key = RESET_FOOTER_KEY)

    data class GroupHeader(val name: String) : ConfigAdapterModel(key = name)

    data class StringConfig(
        val item: ConfigItem<String>,
        val value: String,
        val isModified: Boolean,
        val metadata: DisplayMetadata
    ) : ConfigAdapterModel(key = item.key)

    data class NumberConfig<T: Number>(
        val item: ConfigItem<T>,
        val value: T,
        val isModified: Boolean,
        val metadata: DisplayMetadata
    ) : ConfigAdapterModel(key = item.key)

    data class BooleanConfig(
        val item: ConfigItem<Boolean>,
        val value: Boolean,
        val isModified: Boolean,
        val metadata: DisplayMetadata
    ) : ConfigAdapterModel(key = item.key)

    data class DateConfig(
        val item: ConfigItem<Date>,
        val value: Date,
        val isModified: Boolean,
        val metadata: DisplayMetadata
    ) : ConfigAdapterModel(key = item.key)

    override fun equals(other: Any?): Boolean {
        return other is ConfigAdapterModel && this.toString() == other.toString()
    }
}