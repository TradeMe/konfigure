package nz.co.trademe.konfigure.android.ui.adapter.viewholder

import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import nz.co.trademe.konfigure.android.databinding.ViewHolderNumberBinding
import nz.co.trademe.konfigure.android.ui.adapter.ConfigAdapterModel
import nz.co.trademe.konfigure.android.ui.dialog.EditConfigDialogFragment

internal class NumberConfigViewHolder(
    parent: ViewGroup
) : BaseViewHolder<ViewHolderNumberBinding>(parent.inflate(ViewHolderNumberBinding::inflate)) {

    fun bind(model: ConfigAdapterModel.NumberConfig<*>) {
        with(binding) {
            titleTextView.text = model.metadata.title
            titleTextView.showAsModified(model.isModified)

            descriptionTextView.text = model.metadata.description

            currentValueTextView.text = model.value.toString()
            currentValueTextView.applyMonospaceFont()

            itemView.setOnClickListener {
                EditConfigDialogFragment.start(
                    model.item,
                    model.value.toString(),
                    (itemView.context as AppCompatActivity).supportFragmentManager
                )
            }
        }
    }
}