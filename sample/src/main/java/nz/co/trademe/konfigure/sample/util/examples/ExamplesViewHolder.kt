package nz.co.trademe.konfigure.sample.util.examples

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import nz.co.trademe.konfigure.sample.examples.Example
import kotlinx.android.synthetic.main.viewholder_example.*

class ExamplesViewHolder(
    override val containerView: View
): RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(example: Example) {
        exampleTitleTextView.text = example.title
        exampleDescriptionTextView.text = example.description

        itemView.setOnClickListener {
            example.onClick(it.context as AppCompatActivity)
        }
    }
}