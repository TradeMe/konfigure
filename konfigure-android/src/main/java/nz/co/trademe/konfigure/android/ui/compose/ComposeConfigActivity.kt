package nz.co.trademe.konfigure.android.ui.compose

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import nz.co.trademe.konfigure.android.extensions.applicationConfig
import nz.co.trademe.konfigure.android.ui.compose.theme.KonfigureTheme
import nz.co.trademe.konfigure.android.ui.view.ConfigPresenter

/**
 * Basic activity hosting the [ComposeScreen]. This can be extended
 * to add basic functionality, like filtering of config items. Alternatively, [ComposeScreen]
 * can be used independently.
 *
 * To use, simply call [ComposeConfigActivity.start]
 */
open class ComposeConfigActivity : AppCompatActivity() {

    private lateinit var presenter: ConfigPresenter

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = ConfigPresenter(applicationContext.applicationConfig)
        presenter.search() // Get initial models

        setContent {
            val models by presenter.models.collectAsStateWithLifecycle(initialValue = emptyList())

            KonfigureTheme {
                ConfigScreen(models = models)
            }
        }
    }

    companion object {

        @JvmStatic
        fun start(activity: AppCompatActivity) =
            activity.startActivity(Intent(activity, ComposeConfigActivity::class.java))
    }
}