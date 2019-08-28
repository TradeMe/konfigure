package nz.co.trademe.konfigure.android.ui.adapter.viewholder

import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.view_holder_long.*
import nz.co.trademe.konfigure.android.R
import nz.co.trademe.konfigure.android.ui.adapter.ConfigAdapterModel
import nz.co.trademe.konfigure.android.ui.dialog.EditConfigDialogFragment
import nz.co.trademe.konfigure.android.ui.dialog.EditConfigType

internal class LongConfigViewHolder(
    parent: ViewGroup
) : BaseViewHolder(parent.inflate(R.layout.view_holder_long)) {

    fun bind(model: ConfigAdapterModel.LongConfig) {
        titleTextView.text = model.metadata.title
        titleTextView.showAsModified(model.isModified)

        descriptionTextView.text = model.metadata.description

        currentValueTextView.text = model.value.toString()
        currentValueTextView.applyMonospaceFont()

        itemView.setOnClickListener {
            EditConfigDialogFragment.start(
                    model.item,
                    EditConfigType.NUMBER,
                    model.value.toString(),
                    (itemView.context as AppCompatActivity).supportFragmentManager)
        }
    }
}