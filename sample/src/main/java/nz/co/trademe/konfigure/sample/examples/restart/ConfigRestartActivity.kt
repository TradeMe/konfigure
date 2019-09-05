package nz.co.trademe.konfigure.sample.examples.restart

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import nz.co.trademe.konfigure.android.extensions.applicationConfig
import nz.co.trademe.konfigure.android.ui.ConfigActivity
import nz.co.trademe.konfigure.sample.examples.restart.config.RestartMetadata
import nz.co.trademe.konfigure.sample.examples.restart.util.showRestartSnackbar

/**
 * This activity demonstrates the additional [RestartMetadata.requiresRestart] property, which when
 * applied to a config item allows us to differentiate config overrides which require restarts, and
 * show UI accordingly.
 */
class ConfigRestartActivity : ConfigActivity() {

    private var snackbar: Snackbar? = null

    @ExperimentalCoroutinesApi
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        observeConfigChanges()
    }

    /**
     * This function observes config changes which have [RestartMetadata] attached to them. It keeps track
     * of keys which are overridden, and shows/hides a snackbar when applicable.
     */
    @ExperimentalCoroutinesApi
    private fun observeConfigChanges() {
        // Observe changes and determine if a restart is required
        lifecycleScope.launch {
            applicationConfig.changes
                .asFlow()
                // Only keep items which have restart information, and require tracking
                .filter { (it.metadata as? RestartMetadata)?.requiresRestart == true }

                // Accumulate changes
                .scan(initial = HashMap<String, String>()) { acc, event ->
                    if (acc.containsKey(event.key)) {
                        val originalValue = acc[event.key]

                        if (event.newValue.toString() == originalValue) {
                            acc.remove(event.key)
                        }
                    } else {
                        // Store the original value
                        acc[event.key] = event.oldValue.toString()
                    }

                    acc
                }

                // If the map accumulated restart requiring changes aren't empty, we should
                // show a snackbar, otherwise we shouldn't
                .map { it.isNotEmpty() }

                // Only take changes
                .distinctUntilChanged()

                .collect { requiresRestart ->
                    withContext(Dispatchers.Main) {
                        changeSnackbarVisibility(requiresRestart)
                    }
                }
        }
    }

    private fun changeSnackbarVisibility(restartRequired: Boolean) {
        snackbar = if (restartRequired && snackbar == null) {
            showRestartSnackbar()
        } else {
            snackbar?.dismiss()
            null
        }
    }
}