package nz.co.trademe.konfigure.android.ui.adapter.viewholder

import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import nz.co.trademe.konfigure.android.R

internal class DividerViewHolder(
    parent: ViewGroup
) : BaseViewHolder(parent.inflate(R.layout.view_holder_divider)) {

    init {
        itemView.setBackgroundColor(AppCompatResources.getColorStateList(itemView.context, R.color.color_divider).defaultColor)
    }
}