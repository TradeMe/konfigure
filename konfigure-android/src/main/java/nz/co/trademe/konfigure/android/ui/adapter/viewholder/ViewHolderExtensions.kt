package nz.co.trademe.konfigure.android.ui.adapter.viewholder

import android.graphics.Typeface
import androidx.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import nz.co.trademe.konfigure.android.R

/**
 * Helper function for allowing simple ViewHolder view inflation
 */
internal fun <VB : ViewBinding> ViewGroup.inflate(inflate: (LayoutInflater, ViewGroup, Boolean) -> VB): VB {
    return inflate(LayoutInflater.from(context), this, false)
}

internal fun TextView.applyMonospaceFont() {
    val typeface = Typeface.createFromAsset(context.assets, "fonts/RobotoMono-Regular.ttf")
    setTypeface(typeface)
}

internal fun TextView.showAsModified(isModified: Boolean) {
    setCompoundDrawablesWithIntrinsicBounds(
            if (isModified) R.drawable.shape_modified_circle else 0,
            0,
            0,
            0
    )
}