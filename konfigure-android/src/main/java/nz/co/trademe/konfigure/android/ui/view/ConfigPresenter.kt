package nz.co.trademe.konfigure.android.ui.view

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import nz.co.trademe.konfigure.Config
import nz.co.trademe.konfigure.android.ui.DisplayMetadata
import nz.co.trademe.konfigure.android.ui.adapter.ConfigAdapterModel
import nz.co.trademe.konfigure.model.ConfigItem

internal class ConfigPresenter(
    private val config: Config,
    private val filters: Set<ConfigView.Filter>,
    private val onModelsChanges: (models: List<ConfigAdapterModel>) -> Unit,
    private val parentJob: Job = Job()
) : CoroutineScope by GlobalScope + Dispatchers.IO + parentJob {

    private var searchTerm: String = ""

    init {
        search(searchTerm)

        launch {
            config.changes
                .collect { search(searchTerm) }
        }
    }

    /**
     * Perform a search on the config items given a search string. This does the search on an IO dispatcher, and posts results to the main thread.
     *
     * @param searchString The string to search using.
     */
    fun search(searchString: String) {
        launch {
            // Search modified items and map to models
            val modifiedItems = config.modifiedItems
                .search(searchString)
                .mapModifiedSectionAdapterModels()

            // Search all items and map to models
            val allItems = config.configItems
                .search(searchString)
                .mapToAdapterModels()

            // Switch scope to emit results
            withContext(Dispatchers.Main) {
                this@ConfigPresenter.searchTerm = searchString
                onModelsChanges(modifiedItems + allItems)
            }
        }
    }

    fun destroy() {
        parentJob.cancel()
    }

    /**
     * This function performs a search on the receiving list. It applies any given filter to each item, then filters items based off the [filterBy] function.
     * All searching is performed on the dispatcher used by the parent scope.
     *
     * @receiver The list to filter
     * @return The filtered list
     */
    private suspend fun List<ConfigItem<*>>.search(searchString: String): List<ConfigItem<*>> = coroutineScope {
        return@coroutineScope filter { item ->
                filters
                    .map { it.shouldKeepItem(item) }
                    .ifEmpty { listOf(true) }
                    .reduce { acc, shouldFilter -> acc || shouldFilter }
            }
            .filter {
                // Perform the search
                searchString.isEmpty() || it.filterBy(searchString)
            }
    }

    /**
     * This function maps a list of [ConfigItem]s into a list of [ConfigAdapterModel] for display. It does so by grouping elements based on [DisplayMetadata] applied to each config item.
     * All mapping is performed on the dispatcher used by the parent scope.
     *
     * @receiver The list to map
     * @return The adapter models to display
     */
    private suspend fun List<ConfigItem<*>>.mapToAdapterModels(): List<ConfigAdapterModel> = coroutineScope {
        var topDividerSkipped = false

        return@coroutineScope groupBy { (it.metadata as? DisplayMetadata)?.group }
            .flatMap { (group, items) ->
                // Construct grouped items
                listOfNotNull(
                    group?.let { ConfigAdapterModel.GroupHeader(group) },
                    *items.mapToModel()
                )
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

    /**
     * This function maps a list of [ConfigItem]s into a list of [ConfigAdapterModel] for display. It does so by grouping all elements together, then applying an overrides header and reset footer.
     * All mapping is performed on the dispatcher used by the parent scope.
     *
     * @receiver The list to map
     * @return The adapter models to display
     */
    private suspend fun List<ConfigItem<*>>.mapModifiedSectionAdapterModels(): List<ConfigAdapterModel> = coroutineScope {
        return@coroutineScope mapToModel()
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

    /**
     * Simple function for determining whether or not the receiving [ConfigItem] should be filtered, i.e kept.
     */
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