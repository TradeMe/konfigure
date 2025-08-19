package nz.co.trademe.konfigure.android.ui.compose

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import nz.co.trademe.konfigure.android.extensions.applicationConfig
import nz.co.trademe.konfigure.android.ui.adapter.ConfigAdapterModel
import nz.co.trademe.konfigure.android.ui.compose.components.SearchableTopBar
import nz.co.trademe.konfigure.android.ui.compose.components.SearchableTopBarState
import nz.co.trademe.konfigure.android.ui.compose.items.BooleanConfig
import nz.co.trademe.konfigure.android.ui.compose.items.DateConfig
import nz.co.trademe.konfigure.android.ui.compose.items.GroupHeader
import nz.co.trademe.konfigure.android.ui.compose.items.NumberConfig
import nz.co.trademe.konfigure.android.ui.compose.items.ResetToDefaultItem
import nz.co.trademe.konfigure.android.ui.compose.items.StringConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigScreen(
    models: List<ConfigAdapterModel>?,
    modifier: Modifier = Modifier,
    onConfigChanged: (key: String?, value: Any) -> Unit,
    onSearch: (query: String) -> Unit,
    booleanConfig: @Composable (config: ConfigAdapterModel.BooleanConfig) -> Unit = { config ->
        BooleanConfig(
            title = config.metadata.title,
            description = config.metadata.description,
            value = config.value,
            isModified = config.isModified,
            onValueChange = { newValue ->
                onConfigChanged(config.key, newValue)
            }
        )
    },
    dateConfig: @Composable (config: ConfigAdapterModel.DateConfig) -> Unit = { config ->
        DateConfig(
            title = config.metadata.title,
            description = config.metadata.description,
            value = config.value,
            isModified = config.isModified,
            onConfigChange = { newValue ->
                onConfigChanged(config.key, newValue)
            }
        )
    },
    divider: @Composable () -> Unit = {
        HorizontalDivider()
    },
    groupHeader: @Composable (header: ConfigAdapterModel.GroupHeader) -> Unit = { header ->
        GroupHeader(name = header.name)
    },
    numberConfig: @Composable (config: ConfigAdapterModel.NumberConfig<*>) -> Unit = { config ->
        NumberConfig(
            title = config.metadata.title,
            description = config.metadata.description,
            value = config.value,
            isModified = config.isModified,
            onConfigChange = { newValue ->
                onConfigChanged(config.key, newValue)
            },
        )
    },
    resetToDefaultFooter: @Composable () -> Unit = {
        val context = LocalContext.current
        ResetToDefaultItem { context.applicationConfig.clearOverrides() }
    },
    stringConfig: @Composable (config: ConfigAdapterModel.StringConfig) -> Unit = { config ->
        StringConfig(
            title = config.metadata.title,
            description = config.metadata.description,
            value = config.value,
            isModified = config.isModified,
            onConfigChange = { newValue ->
                onConfigChanged(config.key, newValue)
            },
        )
    },
) {
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val lazyListState = rememberLazyListState()

    // Search state
    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val searchState = remember(isSearchActive, searchQuery) {
        SearchableTopBarState(
            isSearchActive = isSearchActive,
            searchQuery = searchQuery,
            onIsSearchActiveChange = { newIsSearchActive -> isSearchActive = newIsSearchActive },
            onQueryChange = { newQuery -> searchQuery = newQuery },
        )
    }

    // Hide keyboard and clear focus when scrolling
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(lazyListState.isScrollInProgress) {
        if (lazyListState.isScrollInProgress) {
            focusManager.clearFocus()
            keyboardController?.hide()
        }
    }

    LaunchedEffect(searchQuery) {
        onSearch(searchQuery)
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SearchableTopBar(
                state = searchState,
                scrollBehavior = scrollBehavior,
                onBack = { backDispatcher?.onBackPressed() }
            )
        },
        content = { innerPadding ->
            LazyColumn(
                state = lazyListState,
                contentPadding = innerPadding,
            ) {
                models?.let { nonNullModels ->
                    items(nonNullModels) { model ->
                        when (model) {
                            // Config items
                            is ConfigAdapterModel.NumberConfig<*> -> numberConfig(model)
                            is ConfigAdapterModel.StringConfig -> stringConfig(model)
                            is ConfigAdapterModel.BooleanConfig -> booleanConfig(model)
                            is ConfigAdapterModel.DateConfig -> dateConfig(model)

                            // Non-config items
                            is ConfigAdapterModel.GroupHeader -> groupHeader(model)
                            ConfigAdapterModel.Divider -> divider()
                            ConfigAdapterModel.ResetToDefaultFooter -> resetToDefaultFooter()
                        }
                    }
                }
            }
        }
    )
}

@Preview
@Composable
private fun ConfigScreenPreview() {
    MaterialTheme {
        ConfigScreen(
            models = emptyList(),
            onConfigChanged = { _, _ -> },
            onSearch = {},
        )
    }
}