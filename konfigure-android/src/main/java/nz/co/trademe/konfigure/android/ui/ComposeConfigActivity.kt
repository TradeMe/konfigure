package nz.co.trademe.konfigure.android.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import nz.co.trademe.konfigure.android.ui.theme.KonfigureTheme
import nz.co.trademe.konfigure.android.ui.view.ConfigView

/**
 * Basic activity hosting the [ComposeScreen]. This can be extended
 * to add basic functionality, like filtering of config items. Alternatively, [ComposeScreen]
 * can be used independently.
 *
 * To use, simply call [ComposeConfigActivity.start]
 */
open class ComposeConfigActivity : AppCompatActivity() {

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KonfigureTheme {
                ConfigScreen()
            }
        }
    }

    companion object {

        @JvmStatic
        fun start(activity: AppCompatActivity) =
            activity.startActivity(Intent(activity, ComposeConfigActivity::class.java))
    }
}