package nz.co.trademe.konfigure.sample.examples.compose

import androidx.appcompat.app.AppCompatActivity
import nz.co.trademe.konfigure.android.ui.ComposeConfigActivity
import nz.co.trademe.konfigure.sample.examples.Example
import nz.co.trademe.konfigure.android.ui.ConfigActivity

object ComposeExample: Example {
    override val title: String
        get() = "Compose Activity Usage"
    override val description: String
        get() = """
            This example shows the most basic usage of Konfigure, using the pre-built ComposeConfigActivity.
        """.trimIndent()

    override fun onClick(activity: AppCompatActivity) {
        ComposeConfigActivity.start(activity)
    }
}