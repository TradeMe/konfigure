package nz.co.trademe.konfigure.android.ui.adapter

import android.view.LayoutInflater
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import kotlinx.coroutines.NonDisposableHandle.parent
import nz.co.trademe.konfigure.android.ui.adapter.viewholder.BooleanConfigViewHolder
import nz.co.trademe.konfigure.android.ui.adapter.viewholder.DateConfigViewHolder
import nz.co.trademe.konfigure.android.ui.adapter.viewholder.DividerViewHolder
import nz.co.trademe.konfigure.android.ui.adapter.viewholder.HeaderViewHolder
import nz.co.trademe.konfigure.android.ui.adapter.viewholder.NumberConfigViewHolder
import nz.co.trademe.konfigure.android.ui.adapter.viewholder.ResetToDefaultFooterViewHolder
import nz.co.trademe.konfigure.android.ui.adapter.viewholder.StringConfigViewHolder

/**
 * Adapter for displaying editable config items
 */
internal class ConfigAdapter(
    private val resetAllCallback: () -> Unit
) : ListAdapter<ConfigAdapterModel, RecyclerView.ViewHolder>(CONFIG_DIFF_CALLBACK) {

    enum class ViewType {
        HEADER,
        STRING,
        NUMBER,
        BOOLEAN,
        DATE,
        DIVIDER,
        RESET_FOOTER
    }

    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is ConfigAdapterModel.GroupHeader -> ViewType.HEADER
            is ConfigAdapterModel.StringConfig -> ViewType.STRING
            is ConfigAdapterModel.NumberConfig<*> -> ViewType.NUMBER
            is ConfigAdapterModel.BooleanConfig -> ViewType.BOOLEAN
            is ConfigAdapterModel.DateConfig -> ViewType.DATE
            is ConfigAdapterModel.ResetToDefaultFooter -> ViewType.RESET_FOOTER
            ConfigAdapterModel.Divider -> ViewType.DIVIDER
        }.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (ViewType.values()[viewType]) {
            ViewType.HEADER -> HeaderViewHolder(parent)
            ViewType.STRING -> StringConfigViewHolder(parent)
            ViewType.NUMBER -> NumberConfigViewHolder(parent)
            ViewType.BOOLEAN -> BooleanConfigViewHolder(parent)
            ViewType.DATE -> DateConfigViewHolder(parent)
            ViewType.DIVIDER -> DividerViewHolder(parent)
            ViewType.RESET_FOOTER -> ResetToDefaultFooterViewHolder(parent)
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                holder.bind((getItem(position) as ConfigAdapterModel.GroupHeader).name)
            }
            is StringConfigViewHolder -> {
                val model = getItem(position) as ConfigAdapterModel.StringConfig
                holder.bind(model)
            }
            is NumberConfigViewHolder -> {
                val model = getItem(position) as ConfigAdapterModel.NumberConfig<*>
                holder.bind(model)
            }
            is BooleanConfigViewHolder -> {
                val model = getItem(position) as ConfigAdapterModel.BooleanConfig
                holder.bind(model)
            }
            is ResetToDefaultFooterViewHolder -> {
                holder.bind(resetAllCallback)
            }
            is DateConfigViewHolder -> {
                val model = getItem(position) as ConfigAdapterModel.DateConfig
                holder.bind(model)
            }
            is DividerViewHolder -> { /* Nothing to bind */
            }
            else ->
                throw IllegalArgumentException("Unknown viewholder type: ${holder::class.java.simpleName}")
        }
    }

    companion object {
        val CONFIG_DIFF_CALLBACK = object : DiffUtil.ItemCallback<ConfigAdapterModel>() {

            override fun areItemsTheSame(oldItem: ConfigAdapterModel, newItem: ConfigAdapterModel): Boolean =
                oldItem.key == newItem.key

            override fun areContentsTheSame(oldItem: ConfigAdapterModel, newItem: ConfigAdapterModel): Boolean =
                oldItem.toString() == newItem.toString()
        }
    }
}