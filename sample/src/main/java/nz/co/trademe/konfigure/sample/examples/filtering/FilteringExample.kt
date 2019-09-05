package nz.co.trademe.konfigure.sample.examples.filtering

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import nz.co.trademe.konfigure.sample.examples.Example

object FilteringExample: Example {
    override val title: String
        get() = "Config Filtering"
    override val description: String
        get() = "A demonstration of simple config filtering"

    override fun onClick(activity: AppCompatActivity) {
        activity.startActivity(Intent(activity, FilteredConfigActivity::class.java))
    }
}