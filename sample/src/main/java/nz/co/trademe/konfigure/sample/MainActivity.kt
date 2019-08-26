package nz.co.trademe.konfigure.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import nz.co.trademe.konfigure.sample.examples.AllExamples
import nz.co.trademe.konfigure.sample.util.examples.ExamplesAdapter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        exampleRecyclerView.layoutManager = LinearLayoutManager(this)
        exampleRecyclerView.adapter = ExamplesAdapter().also {
            it.submitList(AllExamples)
        }
    }
}
