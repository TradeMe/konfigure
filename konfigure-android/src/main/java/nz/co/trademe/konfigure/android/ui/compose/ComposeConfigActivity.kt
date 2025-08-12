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
import nz.co.trademe.konfigure.model.ConfigItem
import kotlin.reflect.KClass

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
                ConfigScreen(
                    models = models,
                    onConfigChanged = { key, value ->
                        with(applicationConfig) {
                            // Locate config by the given key
                            val item = configItems.find { it.key == key }
                            item?.let {
                                // Cast to the required types
                                @Suppress("UNCHECKED_CAST")
                                val typedItem = item as ConfigItem<Any>
                                @Suppress("UNCHECKED_CAST")
                                val kClass = value::class as KClass<Any>

                                // Set value of config
                                setValueOf(
                                    item = typedItem,
                                    itemClass = kClass,
                                    newValue = value,
                                )
                            }
                        }
                    },
                )
            }
        }
    }

    companion object {

        @JvmStatic
        fun start(activity: AppCompatActivity) =
            activity.startActivity(Intent(activity, ComposeConfigActivity::class.java))
    }
}