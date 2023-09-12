package nz.co.trademe.konfigure.android.ui.adapter.viewholder

import android.view.ViewGroup
import nz.co.trademe.konfigure.android.databinding.ViewHolderResetToDefaultBinding

internal class ResetToDefaultFooterViewHolder(
    parent: ViewGroup
) : BaseViewHolder<ViewHolderResetToDefaultBinding>(parent.inflate(ViewHolderResetToDefaultBinding::inflate)) {

    fun bind(resetCallback: () -> Unit) {
        binding.resetToDefaultButton.setOnClickListener {
            resetCallback()
        }
    }
}