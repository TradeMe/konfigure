package nz.co.trademe.konfigure.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import nz.co.trademe.konfigure.sample.databinding.ActivityMainBinding
import nz.co.trademe.konfigure.sample.examples.AllExamples
import nz.co.trademe.konfigure.sample.util.examples.ExamplesAdapter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(ActivityMainBinding.inflate(layoutInflater)) {
            setContentView(root)

            exampleRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            exampleRecyclerView.adapter = ExamplesAdapter().also {
                it.submitList(AllExamples)
            }
        }
    }
}
