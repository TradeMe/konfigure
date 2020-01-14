package nz.co.trademe.konfigure.android.ui.adapter.viewholder

import android.annotation.SuppressLint
import android.view.ViewGroup
import kotlinx.android.synthetic.main.view_holder_string.*
import nz.co.trademe.konfigure.android.R
import nz.co.trademe.konfigure.android.ui.adapter.ConfigAdapterModel
import nz.co.trademe.konfigure.android.ui.dialog.EditConfigDialogFragment
import nz.co.trademe.konfigure.android.ui.ConfigActivity

internal class StringConfigViewHolder(
    parent: ViewGroup
) : BaseViewHolder(parent.inflate(R.layout.view_holder_string)) {

    fun bind(model: ConfigAdapterModel.StringConfig) {
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
                    (itemView.context as ConfigActivity).supportFragmentManager)
        }
    }
}