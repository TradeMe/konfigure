package nz.co.trademe.konfigure.android.ui.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import nz.co.trademe.konfigure.android.extensions.applicationConfig
import nz.co.trademe.konfigure.android.ui.adapter.ConfigAdapter
import nz.co.trademe.konfigure.android.ui.adapter.ConfigAdapterModel
import nz.co.trademe.konfigure.model.ConfigItem

/**
 * RecyclerView which uses the applications [Config] instance for displaying and modifying config items
 */
class ConfigView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr), CoroutineScope by GlobalScope + Dispatchers.IO {

    private val presenter: ConfigPresenter

    private var modelObservationJob: Job = Job()

    init {
        // Setup RecyclerView
        adapter = ConfigAdapter(
            resetAllCallback = { context.applicationConfig.clearOverrides() })

        layoutManager = LinearLayoutManager(context, VERTICAL, false)

        presenter = ConfigPresenter(
            config = context.applicationConfig
        )
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.search()
        launch(modelObservationJob) {
            presenter.models.collect { models ->
                withContext(Dispatchers.Main) {
                    onNewModels(models)
                }
            }
        }

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        modelObservationJob.cancelChildren()
    }


    fun addFilter(filter: Filter) {
        presenter.addFilter(filter)
    }

    fun search(searchTerm: String) {
        presenter.search(searchString = searchTerm)
    }

    private fun onNewModels(models: List<ConfigAdapterModel>) {
        (adapter as ConfigAdapter).submitList(models)
    }

    interface Filter {

        fun shouldKeepItem(item: ConfigItem<*>): Boolean
    }
}