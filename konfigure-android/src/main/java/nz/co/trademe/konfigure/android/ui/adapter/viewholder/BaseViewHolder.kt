package nz.co.trademe.konfigure.android.ui.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

/**
 * Simple base for ViewHolders which implements [LayoutContainer]
 */
internal open class BaseViewHolder(
    override val containerView: View
): RecyclerView.ViewHolder(containerView), LayoutContainer