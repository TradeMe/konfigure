package nz.co.trademe.konfigure.android.ui.adapter.viewholder

import android.view.ViewGroup
import kotlinx.android.synthetic.main.view_holder_reset_to_default.*
import nz.co.trademe.konfigure.android.R

internal class ResetToDefaultFooterViewHolder(
    parent: ViewGroup
) : BaseViewHolder(parent.inflate(R.layout.view_holder_reset_to_default)) {

    fun bind(resetCallback: () -> Unit) {
        resetToDefaultButton.setOnClickListener {
            resetCallback()
        }
    }
}