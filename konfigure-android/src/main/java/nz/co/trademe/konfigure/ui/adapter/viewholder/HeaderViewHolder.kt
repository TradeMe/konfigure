package nz.co.trademe.konfigure.ui.adapter.viewholder

import android.view.ViewGroup
import kotlinx.android.synthetic.main.view_holder_header.*
import nz.co.trademe.konfigure.R

internal class HeaderViewHolder(
    parent: ViewGroup
) : BaseViewHolder(parent.inflate(R.layout.view_holder_header)) {

    fun bind(title: String) {
        titleTextView.text = title
    }
}