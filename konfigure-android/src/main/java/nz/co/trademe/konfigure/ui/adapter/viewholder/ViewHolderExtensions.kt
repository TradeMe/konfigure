package nz.co.trademe.konfigure.ui.adapter.viewholder

import android.graphics.Typeface
import androidx.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import nz.co.trademe.konfigure.R

/**
 * Helper function for allowing simple ViewHolder view inflation
 */
internal fun ViewGroup.inflate(@LayoutRes resource: Int): View =
    LayoutInflater.from(context).inflate(resource, this, false)

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