package nz.co.trademe.konfigure.android.ui.compose.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults.inputFieldColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nz.co.trademe.konfigure.android.R
import nz.co.trademe.konfigure.android.ui.compose.theme.KonfigureTheme

@Stable
class SearchableTopBarState(
    val isSearchActive: Boolean,
    val searchQuery: String,
    val onIsSearchActiveChange: (Boolean) -> Unit,
    val onQueryChange: (String) -> Unit,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchableTopBar(
    state: SearchableTopBarState,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    onBack: () -> Unit,
) {
    TopAppBar(
        title = {
            Box(modifier = modifier.fillMaxWidth()) {
                AnimatedVisibility(
                    visible = !state.isSearchActive,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier.align(Alignment.CenterStart),
                ) {
                    Text(
                        text = stringResource(R.string.configuration),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                AnimatedVisibility(
                    visible = state.isSearchActive,
                    enter = expandHorizontally(expandFrom = Alignment.End) + fadeIn(),
                    exit = shrinkHorizontally(shrinkTowards = Alignment.End) + fadeOut(),
                    modifier = Modifier
                        .clip(RoundedCornerShape(percent = 50))
                        .align(Alignment.CenterEnd),
                ) {
                    InputField(
                        query = state.searchQuery,
                        onQueryChange = state.onQueryChange,
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    if (state.isSearchActive) {
                        state.onIsSearchActiveChange(false)
                        state.onQueryChange("")
                    } else {
                        onBack()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = stringResource(R.string.content_description_searchable_top_bar_back_button)
                )
            }
        },
        actions = {
            IconButton(
                onClick = {
                    when {
                        state.isSearchActive && state.searchQuery.isEmpty() -> state.onIsSearchActiveChange(false)
                        state.isSearchActive && state.searchQuery.isNotEmpty() -> state.onQueryChange("")
                        else -> state.onIsSearchActiveChange(true)
                    }
                }
            ) {
                Icon(
                    imageVector = if (state.isSearchActive) Icons.Default.Clear else Icons.Default.Search,
                    contentDescription = when {
                        state.isSearchActive && state.searchQuery.isEmpty() -> stringResource(R.string.content_description_searchable_top_bar_close_button)
                        state.isSearchActive && state.searchQuery.isNotEmpty() -> stringResource(R.string.content_description_searchable_top_bar_clear_button)
                        else -> stringResource(R.string.content_description_searchable_top_bar_search_button)
                    },
                )
            }
        },
        scrollBehavior = scrollBehavior,
    )
}

@ExperimentalMaterial3Api
@Composable
internal fun BoxScope.InputField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    colors: TextFieldColors = inputFieldColors(),
) {
    val interactionSource = remember { MutableInteractionSource() }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val textStyle = MaterialTheme.typography.bodyLarge

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .align(Alignment.BottomCenter)
            .defaultMinSize(minHeight = 48.dp)
            .fillMaxWidth()
            .background(color = colors.focusedContainerColor, shape = RoundedCornerShape(percent = 50))
            .focusRequester(focusRequester),
        singleLine = true,
        cursorBrush = SolidColor(colors.cursorColor),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
        interactionSource = interactionSource,
        textStyle = textStyle,
        decorationBox = @Composable { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.CenterStart,
            ) {
                innerTextField()
                if (query.isEmpty()) {
                    Text(
                        text = stringResource(R.string.placeholder_searchable_top_bar_text_field),
                        style = textStyle,
                        color = textStyle.color.copy(alpha = 0.75f)
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun SearchableTopBar_SearchInactive_Preview() {
    KonfigureTheme {
        val state = SearchableTopBarState(
            isSearchActive = false,
            searchQuery = "",
            onIsSearchActiveChange = {},
            onQueryChange = {},
        )
        SearchableTopBar(
            state = state,
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            onBack = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun SearchableTopBar_SearchActive_Preview() {
    KonfigureTheme {
        val state = SearchableTopBarState(
            isSearchActive = true,
            searchQuery = "",
            onIsSearchActiveChange = {},
            onQueryChange = {},
        )
        SearchableTopBar(
            state = state,
            scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            onBack = {}
        )
    }
}