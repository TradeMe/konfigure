package nz.co.trademe.konfigure.android.ui.adapter.viewholder

import android.view.ViewGroup
import nz.co.trademe.konfigure.android.databinding.ViewHolderBooleanBinding
import nz.co.trademe.konfigure.android.extensions.applicationConfig
import nz.co.trademe.konfigure.android.ui.adapter.ConfigAdapterModel

internal class BooleanConfigViewHolder(
    parent: ViewGroup,
) : BaseViewHolder<ViewHolderBooleanBinding>(parent.inflate(ViewHolderBooleanBinding::inflate)) {

    fun bind(model: ConfigAdapterModel.BooleanConfig) {
        with(binding) {
            itemView.setOnClickListener {
                valueSwitch.toggle()
            }

            titleTextView.text = model.metadata.title
            titleTextView.showAsModified(model.isModified)

            descriptionTextView.text = model.metadata.description

            valueSwitch.setOnCheckedChangeListener(null)
            valueSwitch.isChecked = model.value
            valueSwitch.setOnCheckedChangeListener { _, newValue ->
                itemView.context.applicationConfig.setValueOf(model.item, Boolean::class, newValue)
            }
        }
    }
}