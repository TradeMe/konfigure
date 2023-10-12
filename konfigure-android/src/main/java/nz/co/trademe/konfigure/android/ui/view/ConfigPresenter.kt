package nz.co.trademe.konfigure.android.ui.view

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import nz.co.trademe.konfigure.Config
import nz.co.trademe.konfigure.android.ui.DisplayMetadata
import nz.co.trademe.konfigure.android.ui.adapter.ConfigAdapterModel
import nz.co.trademe.konfigure.model.ConfigChangeEvent
import nz.co.trademe.konfigure.model.ConfigItem
import java.util.Date

private const val EMPTY_SEARCH_TERM = ""

@Suppress("EXPERIMENTAL_API_USAGE")
internal class ConfigPresenter(
    private val config: Config
) {

    private val filters = mutableSetOf<ConfigView.Filter>()

    /**
     * Property acting as a search term relay for triggering async searching
     */
    private val searchTermRelay = MutableStateFlow(EMPTY_SEARCH_TERM)

    /**
     * Property acting as a search trigger for triggering async searching
     */
    private val searchTrigger = MutableSharedFlow<Unit>(replay = 1, onBufferOverflow = BufferOverflow.DROP_LATEST)

    /**
     * Property for emitting config changes as a flow
     */
    private val changeNotifier: Flow<ConfigChangeEvent<*>?> = flow {
        // Emit a dummy change to trigger the first combine callback
        emit(null)

        // Emit all config changes
        emitAll(config.changes)
    }

    /**
     * Property exposed to consumers for observing. This combines the config changes emitted by [Config.changes],
     * and the search term relay. It then performs a search on the IO dispatcher and emits the results
     */
    val models: Flow<List<ConfigAdapterModel>> = combine(
        changeNotifier,
        searchTrigger,
        searchTermRelay
    ) { _, _, searchTerm ->
        performSearch(searchTerm)
    }
        .flowOn(Dispatchers.IO)

    /**
     * Adds a given filter to the internal list of filters and notifies the presenter to re-emit changes.
     */
    fun addFilter(filter: ConfigView.Filter) {
        filters.add(filter)

        // Rerun the last search
        searchTrigger.tryEmit(Unit)
    }

    /**
     * Triggers a search on the config items
     */
    fun search() {
        searchTrigger.tryEmit(Unit)
    }

    /**
     * Perform a search on the config items given a search string.
     *
     * @param searchString The string to search using.
     */
    fun search(searchString: String) {
        searchTermRelay.tryEmit(searchString)
    }

    /**
     * Perform a search on the config items given a search string. This does the search asynchronously, and returns the results
     *
     * @param searchString The string to search using.
     * @return List of adapter models resulting from the search
     */
    private suspend fun performSearch(searchString: String): List<ConfigAdapterModel> = coroutineScope {
        // Search modified items and map to models
        val modifiedItems = config.modifiedItems
            .search(searchString)
            .mapModifiedSectionAdapterModels()

        // Search all items and map to models
        val allItems = config.configItems
            .search(searchString)
            .mapToAdapterModels()

        return@coroutineScope modifiedItems + allItems
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
            is Int -> toNumberModel<Int>()
            is Long -> toNumberModel<Long>()
            is Float -> toNumberModel<Float>()
            is Double -> toNumberModel<Double>()
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

            is Date -> ConfigAdapterModel.DateConfig(
                item = this as ConfigItem<Date>,
                value = config.getValueOf(this, Date::class),
                isModified = config.modifiedItems.contains(this),
                metadata = metadata as DisplayMetadata
            )

            else -> throw IllegalArgumentException("Unknown type $this")
        }

    @Suppress("UNCHECKED_CAST")
    private inline fun <reified T : Number> ConfigItem<*>.toNumberModel(): ConfigAdapterModel.NumberConfig<T> = ConfigAdapterModel.NumberConfig(
        item = this as ConfigItem<T>,
        value = config.getValueOf(this, T::class),
        isModified = config.modifiedItems.contains(this),
        metadata = metadata as DisplayMetadata
    )

    private fun List<ConfigItem<*>>.mapToModel(): Array<ConfigAdapterModel> =
        map { it.toModel() }.toTypedArray()
}