package nz.co.trademe.konfigure.android.ui.adapter.viewholder

import android.view.ViewGroup
import nz.co.trademe.konfigure.android.databinding.ViewHolderHeaderBinding

internal class HeaderViewHolder(
    parent: ViewGroup
) : BaseViewHolder<ViewHolderHeaderBinding>(parent.inflate(ViewHolderHeaderBinding::inflate)) {

    fun bind(title: String) {
        binding.titleTextView.text = title
    }
}