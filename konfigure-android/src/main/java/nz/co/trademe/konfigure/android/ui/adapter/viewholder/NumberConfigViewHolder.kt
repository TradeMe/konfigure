package nz.co.trademe.konfigure.android.ui.adapter.viewholder

import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.view_holder_number.*
import nz.co.trademe.konfigure.android.R
import nz.co.trademe.konfigure.android.ui.adapter.ConfigAdapterModel
import nz.co.trademe.konfigure.android.ui.dialog.EditConfigDialogFragment

internal class NumberConfigViewHolder(
    parent: ViewGroup
) : BaseViewHolder(parent.inflate(R.layout.view_holder_number)) {

    fun bind(model: ConfigAdapterModel.NumberConfig<*>) {
        titleTextView.text = model.metadata.title
        titleTextView.showAsModified(model.isModified)

        descriptionTextView.text = model.metadata.description

        currentValueTextView.text = model.value.toString()
        currentValueTextView.applyMonospaceFont()

        itemView.setOnClickListener {
            EditConfigDialogFragment.start(
                    model.item,
                    model.value.toString(),
                    (itemView.context as AppCompatActivity).supportFragmentManager)
        }
    }
}