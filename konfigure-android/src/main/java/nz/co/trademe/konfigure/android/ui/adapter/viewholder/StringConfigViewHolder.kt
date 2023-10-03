package nz.co.trademe.konfigure.android.ui.adapter.viewholder

import android.annotation.SuppressLint
import android.view.ViewGroup
import nz.co.trademe.konfigure.android.databinding.ViewHolderStringBinding
import nz.co.trademe.konfigure.android.ui.ConfigActivity
import nz.co.trademe.konfigure.android.ui.adapter.ConfigAdapterModel
import nz.co.trademe.konfigure.android.ui.dialog.EditConfigDialogFragment

internal class StringConfigViewHolder(
    parent: ViewGroup
) : BaseViewHolder<ViewHolderStringBinding>(parent.inflate(ViewHolderStringBinding::inflate)) {

    fun bind(model: ConfigAdapterModel.StringConfig) {
        with (binding) {
            titleTextView.text = model.metadata.title
            titleTextView.showAsModified(model.isModified)

            descriptionTextView.text = model.metadata.description

            @SuppressLint("SetTextI18n")
            currentValueTextView.text = "\"${model.value}\""
            currentValueTextView.applyMonospaceFont()

            itemView.setOnClickListener {
                EditConfigDialogFragment.start(
                    model.item,
                    model.value,
                    (itemView.context as ConfigActivity).supportFragmentManager
                )
            }
        }
    }
}