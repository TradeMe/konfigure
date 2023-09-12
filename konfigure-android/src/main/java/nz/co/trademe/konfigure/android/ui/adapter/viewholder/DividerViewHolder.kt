package nz.co.trademe.konfigure.android.ui.adapter.viewholder

import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import nz.co.trademe.konfigure.android.R
import nz.co.trademe.konfigure.android.databinding.ViewHolderDividerBinding

internal class DividerViewHolder(
    parent: ViewGroup
) : BaseViewHolder<ViewHolderDividerBinding>(parent.inflate(ViewHolderDividerBinding::inflate)) {

    init {
        itemView.setBackgroundColor(AppCompatResources.getColorStateList(itemView.context, R.color.color_divider).defaultColor)
    }
}