package nz.co.trademe.konfigure.android.ui.compose

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import nz.co.trademe.konfigure.android.ui.adapter.ConfigAdapterModel
import nz.co.trademe.konfigure.android.ui.compose.items.BooleanConfig
import nz.co.trademe.konfigure.android.ui.compose.items.GroupHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigScreen(
    models: List<ConfigAdapterModel>?,
    modifier: Modifier = Modifier,
    booleanConfig: @Composable (ConfigAdapterModel.BooleanConfig) -> Unit = { config ->
        BooleanConfig(
            title = config.metadata.title,
            description = config.metadata.description,
            value = config.value,
            isModified = config.isModified,
            onValueChange = { TODO() }
        )
    },
    dateConfig: @Composable (ConfigAdapterModel.DateConfig) -> Unit = { config ->
        Text("Date config: $config")
    },
    divider: @Composable () -> Unit = {
        HorizontalDivider()
    },
    groupHeader: @Composable (ConfigAdapterModel.GroupHeader) -> Unit = { header ->
        GroupHeader(name = header.name)
                                                                        },
    numberConfig: @Composable (ConfigAdapterModel.NumberConfig<*>) -> Unit = { config ->
        Text("Number config: $config")
    },
    resetToDefaultFooter: @Composable () -> Unit = {
        Text("Reset to default footer")
    },
    stringConfig: @Composable (ConfigAdapterModel.StringConfig) -> Unit = { config ->
        Text("String config: $config")
    },
) {
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text("Configuration", maxLines = 1, overflow = TextOverflow.Ellipsis)
                },
                navigationIcon = {
                    IconButton(onClick = { backDispatcher?.onBackPressed() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { TODO() }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        content = { innerPadding ->
            LazyColumn(contentPadding = innerPadding) {
                models?.let { nonNullModels ->
                    items(nonNullModels) { model ->
                        when (model) {
                            is ConfigAdapterModel.BooleanConfig -> booleanConfig(model)
                            is ConfigAdapterModel.DateConfig -> dateConfig(model)
                            ConfigAdapterModel.Divider -> divider()
                            is ConfigAdapterModel.GroupHeader -> groupHeader(model)
                            is ConfigAdapterModel.NumberConfig<*> -> numberConfig(model)
                            ConfigAdapterModel.ResetToDefaultFooter -> resetToDefaultFooter()
                            is ConfigAdapterModel.StringConfig -> stringConfig(model)
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
//        ConfigScreen()
    }
}