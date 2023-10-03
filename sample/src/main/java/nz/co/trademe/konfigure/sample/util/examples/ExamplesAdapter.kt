package nz.co.trademe.konfigure.sample.util.examples

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import nz.co.trademe.konfigure.sample.R
import nz.co.trademe.konfigure.sample.databinding.ViewholderExampleBinding
import nz.co.trademe.konfigure.sample.examples.Example

class ExamplesAdapter: ListAdapter<Example, ExamplesViewHolder>(EXAMPLES_DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamplesViewHolder {
        val binding = ViewholderExampleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExamplesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExamplesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {

        private val EXAMPLES_DIFF_CALLBACK = object: DiffUtil.ItemCallback<Example>() {
            override fun areItemsTheSame(oldItem: Example, newItem: Example): Boolean =
                oldItem.title == newItem.title

            @SuppressLint("DiffUtilEquals") // Suppressed as all Examples are objects under the hood
            override fun areContentsTheSame(oldItem: Example, newItem: Example): Boolean =
                oldItem == newItem
        }

    }
}