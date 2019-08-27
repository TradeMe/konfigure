package nz.co.trademe.konfigure.sample.examples.basic

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import nz.co.trademe.konfigure.sample.examples.Example
import nz.co.trademe.konfigure.android.ui.ConfigActivity

object BasicExample: Example {
    override val title: String
        get() = "Basic Usage"
    override val description: String
        get() = """
            This example shows the most basic usage of Konfigure, using the pre-built ConfigActivity.
        """.trimIndent()

    override fun onClick(activity: AppCompatActivity) {
        activity.startActivity(Intent(activity, ConfigActivity::class.java))
    }
}