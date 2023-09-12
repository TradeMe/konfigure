package nz.co.trademe.konfigure.android.ui.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * Simple base for ViewHolders
 */
internal open class BaseViewHolder<VB : ViewBinding>(
    protected val binding: VB
): RecyclerView.ViewHolder(binding.root)