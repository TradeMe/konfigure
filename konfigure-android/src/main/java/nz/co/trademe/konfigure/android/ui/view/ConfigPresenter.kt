package nz.co.trademe.konfigure.android.ui.view

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import nz.co.trademe.konfigure.Config
import nz.co.trademe.konfigure.android.ui.DisplayMetadata
import nz.co.trademe.konfigure.android.ui.adapter.ConfigAdapterModel
import nz.co.trademe.konfigure.model.ConfigItem

internal class ConfigPresenter(
    private val config: Config,
    private val filters: Set<ConfigView.Filter>,
    private val onModelsChanges: (List<ConfigAdapterModel>) -> Unit,
    private val parentJob: Job = Job()
): CoroutineScope by GlobalScope + Dispatchers.Main + parentJob {

    private var searchTerm: String = ""

    init {
        search(searchTerm)

        launch {
            config.changes
                .collect { search(searchTerm) }
        }
    }

    fun search(searchString: String) {
        this.searchTerm = searchString
        val allItems = performSearchOnModifiedItems(searchString) + performSearchOnAllItems(searchString)
        onModelsChanges(allItems)
    }

    fun destroy() {
        parentJob.cancel()
    }

    private fun performSearchOnAllItems(searchString: String): List<ConfigAdapterModel> {
        var topDividerSkipped = false

        return config.configItems
            .asSequence()
            // Filter any items via the custom filters supplied, if any
            .filter { item ->
                filters
                    .map { it.shouldKeepItem(item) }
                    .ifEmpty { listOf(true) }
                    .reduce { acc, shouldFilter -> acc || shouldFilter }
            }
            .filter {
                // Perform the search
                searchString.isEmpty() || it.filterBy(searchString)
            }
            .groupBy { (it.metadata as? DisplayMetadata)?.group }
            .flatMap { (group, items) ->
                // Construct grouped items
                listOfNotNull(
                    group?.let { ConfigAdapterModel.GroupHeader(group) },
                    *items.mapToModel())
                    .toMutableList()
                    .apply {
                        // Add divider before header if not the top
                        if (topDividerSkipped) {
                            add(0, ConfigAdapterModel.Divider)
                        } else {
                            topDividerSkipped = true
                        }
                    }
            }
    }

    private fun performSearchOnModifiedItems(searchString: String): List<ConfigAdapterModel> {
        return config.modifiedItems
            .filter {
                // Perform the search
                searchString.isEmpty() || it.filterBy(searchString)
            }
            .mapToModel()
            .run {
                if (isNotEmpty()) {
                    listOf(
                        ConfigAdapterModel.GroupHeader("Overrides"),
                        *this,
                        ConfigAdapterModel.ResetToDefaultFooter
                    )
                } else {
                    emptyList()
                }
            }
    }

    private fun ConfigItem<*>.filterBy(searchString: String): Boolean {
        // Return early if the metadata isn't the correct type
        val metadata = metadata as? DisplayMetadata ?: return false

        // Check if search string is found within metadata entries
        return metadata.title.contains(searchString, ignoreCase = true) || metadata.description.contains(searchString, ignoreCase = true)
    }

    @Suppress("UNCHECKED_CAST")
    private fun ConfigItem<*>.toModel(): ConfigAdapterModel =
        when (defaultValue) {
            is Long -> ConfigAdapterModel.LongConfig(
                item = this as ConfigItem<Long>,
                value = config.getValueOf(this, Long::class),
                isModified = config.modifiedItems.contains(this),
                metadata = metadata as DisplayMetadata
            )
            is Boolean -> ConfigAdapterModel.BooleanConfig(
                item = this as ConfigItem<Boolean>,
                value = config.getValueOf(this, Boolean::class),
                isModified = config.modifiedItems.contains(this),
                metadata = metadata as DisplayMetadata
            )
            is String -> ConfigAdapterModel.StringConfig(
                item = this as ConfigItem<String>,
                value = config.getValueOf(this, String::class),
                isModified = config.modifiedItems.contains(this),
                metadata = metadata as DisplayMetadata
            )
            else -> throw IllegalArgumentException("Unknown type $this")
        }

    private fun List<ConfigItem<*>>.mapToModel(): Array<ConfigAdapterModel> =
        map { it.toModel() }.toTypedArray()
}