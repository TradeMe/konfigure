package nz.co.trademe.konfigure.ui.adapter.viewholder

import android.view.ViewGroup
import kotlinx.android.synthetic.main.view_holder_string.*
import nz.co.trademe.konfigure.ui.adapter.ConfigAdapterModel
import nz.co.trademe.konfigure.ui.dialog.EditConfigDialogFragment
import nz.co.trademe.konfigure.ui.dialog.EditConfigType
import nz.co.trademe.konfigure.ui.ConfigActivity
import nz.co.trademe.konfigure.R

internal class StringConfigViewHolder(
    parent: ViewGroup
) : BaseViewHolder(parent.inflate(R.layout.view_holder_string)) {

    fun bind(model: ConfigAdapterModel.StringConfig) {
        titleTextView.text = model.metadata.title
        titleTextView.showAsModified(model.isModified)

        descriptionTextView.text = model.metadata.description

        currentValueTextView.text = model.value
        currentValueTextView.applyMonospaceFont()

        itemView.setOnClickListener {
            EditConfigDialogFragment.start(
                    model.item,
                    EditConfigType.STRING,
                    model.value,
                    (itemView.context as ConfigActivity).supportFragmentManager)
        }
    }
}