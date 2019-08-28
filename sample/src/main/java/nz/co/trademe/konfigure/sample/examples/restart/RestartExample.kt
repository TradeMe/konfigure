package nz.co.trademe.konfigure.sample.examples.restart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import nz.co.trademe.konfigure.sample.examples.Example

object RestartExample: Example {
    override val title: String
        get() = "Config which requires app restarts"
    override val description: String
        get() = """
            This example shows a more advanced usage of Konfigure, where additional metadata plus custom logic is used to
            determine when an app restart is required, based on metadata on the config entry.
        """.trimIndent()

    override fun onClick(activity: AppCompatActivity) {
        activity.startActivity(Intent(activity, ConfigRestartActivity::class.java))
    }
}