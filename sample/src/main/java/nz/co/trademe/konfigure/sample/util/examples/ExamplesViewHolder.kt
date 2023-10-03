package nz.co.trademe.konfigure.sample.util.examples

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import nz.co.trademe.konfigure.sample.databinding.ViewholderExampleBinding
import nz.co.trademe.konfigure.sample.examples.Example

class ExamplesViewHolder(
    private val binding: ViewholderExampleBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(example: Example) {
        with(binding) {
            exampleTitleTextView.text = example.title
            exampleDescriptionTextView.text = example.description

            itemView.setOnClickListener {
                example.onClick(it.context as AppCompatActivity)
            }
        }
    }
}