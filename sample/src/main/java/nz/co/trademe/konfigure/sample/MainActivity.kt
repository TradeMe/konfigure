package nz.co.trademe.konfigure.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import nz.co.trademe.konfigure.sample.databinding.ActivityMainBinding
import nz.co.trademe.konfigure.sample.examples.AllExamples
import nz.co.trademe.konfigure.sample.util.examples.ExamplesAdapter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(ActivityMainBinding.inflate(layoutInflater)) {
            setContentView(root)

            toolbar.title = "Konfigure"
            exampleRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            exampleRecyclerView.adapter = ExamplesAdapter().also {
                it.submitList(AllExamples)
            }

            ViewCompat.setOnApplyWindowInsetsListener(toolbar) { v: View, insets: WindowInsetsCompat ->
                val insetValues = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
                v.setPadding(insetValues.left, insetValues.top, insetValues.right, v.paddingBottom)
                insets
            }
        }
    }
}
