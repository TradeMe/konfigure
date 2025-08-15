package nz.co.trademe.konfigure.android.ui.compose

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import nz.co.trademe.konfigure.android.extensions.applicationConfig
import nz.co.trademe.konfigure.android.ui.adapter.ConfigAdapterModel
import nz.co.trademe.konfigure.android.ui.compose.components.EditConfigDialog
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
            onDateClick = { TODO() },
            onTimeClick = { TODO() },
            onTodayClick = { TODO() },
        )
    },
    divider: @Composable () -> Unit = {
        HorizontalDivider()
    },
    groupHeader: @Composable (header: ConfigAdapterModel.GroupHeader) -> Unit = { header ->
        GroupHeader(name = header.name)
    },
    numberConfig: @Composable (config: ConfigAdapterModel.NumberConfig<*>, onClick: () -> Unit) -> Unit = { config, onClick ->
        NumberConfig(
            title = config.metadata.title,
            description = config.metadata.description,
            value = config.value,
            isModified = config.isModified,
            onClick = onClick,
        )
    },
    resetToDefaultFooter: @Composable () -> Unit = {
        val context = LocalContext.current
        ResetToDefaultItem { context.applicationConfig.clearOverrides() }
    },
    stringConfig: @Composable (config: ConfigAdapterModel.StringConfig, onClick: () -> Unit) -> Unit = { config, onClick ->
        StringConfig(
            title = config.metadata.title,
            description = config.metadata.description,
            value = config.value,
            isModified = config.isModified,
            onClick = onClick,
        )
    },
) {
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    var currentlyEditing by remember { mutableStateOf<ConfigAdapterModel?>(null) }
    var inputError by remember { mutableStateOf<String?>(null) }

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
                            // Config items
                            is ConfigAdapterModel.NumberConfig<*> -> numberConfig(model) { currentlyEditing = model }
                            is ConfigAdapterModel.StringConfig -> stringConfig(model) { currentlyEditing = model }
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

            currentlyEditing?.let { itemToEdit ->
                EditConfigDialog(
                    item = itemToEdit,
                    inputError = inputError,
                    onDismissRequest = {
                        currentlyEditing = null
                        inputError = null
                    },
                    onSave = { newValue ->
                        // Parse the string back to the correct type and call onConfigChanged
                        try {
                            val parsedValue: Any = when (itemToEdit) {
                                is ConfigAdapterModel.NumberConfig<*> -> {
                                    // Check the runtime type of the value itself
                                    when (itemToEdit.value) {
                                        is Long -> newValue.toLong()
                                        is Int -> newValue.toInt()
                                        is Float -> newValue.toFloat()
                                        is Double -> newValue.toDouble()
                                        else -> newValue // Fallback for unknown number types
                                    }
                                }

                                is ConfigAdapterModel.StringConfig -> newValue
                                else -> newValue // Fallback
                            }
                            onConfigChanged(
                                itemToEdit.key,
                                parsedValue,
                            )
                            currentlyEditing = null
                            inputError = null
                        } catch (e: Exception) {
                            inputError = e.toString()
                        }
                    }
                )
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
        )
    }
}