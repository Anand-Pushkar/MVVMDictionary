package com.example.dictionary.presentation.ui.searchScreen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.dictionary.domain.model.searchSuggestion.SearchSuggestion
import com.example.dictionary.presentation.components.LoadingListShimmer
import com.example.dictionary.presentation.navigation.Screen
import com.example.dictionary.presentation.theme.TabTheme
import com.example.dictionary.util.TAG


@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun SearchScreen(
    isDark: MutableState<Boolean>,
    isNetworkAvailable: MutableState<Boolean>,
    onNavigateToDetailScreen: (String) -> Unit,
    parent: MutableState<String>,
    viewModel: SearchViewModel
) {

    val textFieldValue = viewModel.textFieldValue.value
    val searchSuggestions = viewModel.searchSuggestions.value
    val scaffoldState = rememberScaffoldState()
    val dialogQueue = viewModel.dialogQueue
    val loading = viewModel.loading.value

    TabTheme(
        isDarkTheme = isDark,
        isNetworkAvailable = isNetworkAvailable,
        scaffoldState = scaffoldState,
        dialogQueue = dialogQueue.queue.value,
        displayProgressBar = loading,
        selectedTab = parent
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            backgroundColor = MaterialTheme.colors.primary,
            scaffoldState = scaffoldState,
            snackbarHost = {
                scaffoldState.snackbarHostState
            }
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 48.dp, bottom = 48.dp)
            ) {
                SearchSection(
                    onNavigateToDetailScreen = onNavigateToDetailScreen,
                    textFieldValue = textFieldValue,
                    onQueryChanged = {
                        viewModel.onTriggerEvent(SearchScreenEvent.OnQueryChangedEvent(it))
                    },
                    onTextFieldValueChanged = {
                        viewModel.onTriggerEvent(SearchScreenEvent.OnTextFieldValueChanged(it))
                    },
                    onSearchCleared = {
                        viewModel.onTriggerEvent(SearchScreenEvent.OnSearchCleared)
                    },
                    parent = parent.value,
                )
                SearchSuggestionsList(
                    loading = loading,
                    onNavigateToDetailScreen = onNavigateToDetailScreen,
                    searchSuggestions = searchSuggestions,
                    onQueryChanged = {
                        viewModel.onTriggerEvent(SearchScreenEvent.OnQueryChangedEvent(it))
                    },
                    onTextFieldValueChanged = {
                        viewModel.onTriggerEvent(SearchScreenEvent.OnTextFieldValueChanged(it))
                    },
                    parent = parent.value,
                )
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun SearchSection(
    onNavigateToDetailScreen: (String) -> Unit,
    textFieldValue: TextFieldValue,
    onQueryChanged: (String) -> Unit,
    onTextFieldValueChanged: (TextFieldValue) -> Unit,
    onSearchCleared: () -> Unit,
    parent: String,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        enabled = true,
        value = textFieldValue,
        onValueChange = {
            if (it.text.trim() == "") {
                onSearchCleared()
            } else {
                onQueryChanged(it.text)
                onTextFieldValueChanged(it)
            }
        },
        label = {
            Text(text = "Search", color = MaterialTheme.colors.onPrimary)
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search Icon",
                tint = MaterialTheme.colors.onPrimary
            )
        },
        trailingIcon = {
            if (textFieldValue.text.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onSearchCleared()
                    },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Clear Search Icon",
                        tint = MaterialTheme.colors.onPrimary
                    )
                }
            }
        },
        keyboardActions = KeyboardActions(
            onDone = {
                if (textFieldValue.text != "") {
                    val route = getRoute(
                        parent = parent,
                        query = textFieldValue.text
                    )
                    keyboardController?.hide()
                    onNavigateToDetailScreen(route)
                }
            }
        ),
        singleLine = true,
        textStyle = MaterialTheme.typography.h4,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.primary,
            focusedIndicatorColor = MaterialTheme.colors.secondary,
            cursorColor = MaterialTheme.colors.onPrimary,
            textColor = MaterialTheme.colors.onPrimary
        ),
    )

    DisposableEffect(Unit) {
        focusRequester.requestFocus()
        onDispose { }
    }
}

@Composable
fun SearchSuggestionsList(
    loading: Boolean,
    onNavigateToDetailScreen: (String) -> Unit,
    onQueryChanged: (String) -> Unit,
    onTextFieldValueChanged: (TextFieldValue) -> Unit,
    searchSuggestions: List<SearchSuggestion>,
    parent: String,
) {
    val scrollState = rememberLazyListState()
    if (loading) {
        LoadingListShimmer(
            cardHeight = 24.dp,
            cardWidth = 0.5f,
            lineHeight = 24.dp,
            lineWidth = 0.7f,
            repetition = 10,
            padding = 8.dp,
        )
    } else {
        // lazy column is put inside the else condition because we don't want to show anything while it is loading.
        LazyColumn(
            modifier = Modifier
                .padding(top = 16.dp),
            state = scrollState
        ) {
            itemsIndexed(
                items = searchSuggestions
            ) { index: Int, item: SearchSuggestion ->
                Text(
                    text = item.word,
                    style = MaterialTheme.typography.h3,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = {

                            // this update the query
                            onQueryChanged(item.word)

                            // this update the cursor position
                            onTextFieldValueChanged(
                                TextFieldValue().copy(
                                    text = item.word,
                                    selection = TextRange(item.word.length)
                                )
                            )

                            // pass the selected word with the route
                            val route = getRoute(
                                parent = parent,
                                query = item.word
                            )
                            onNavigateToDetailScreen(route)
                        })
                        .padding(
                            start = 16.dp,
                            top = 8.dp,
                            end = 16.dp,
                            bottom = 8.dp
                        )
                        .wrapContentWidth(Alignment.Start)
                )
            }
        }
    }
}

private fun getRoute(
    parent: String,
    query: String,
): String {
    val route: String
    if (parent == "definition") {
        route = Screen.DEFINITION_DETAIL_ROUTE.withArgs(query)
    } else if (parent == "rhyme") {
        route = Screen.RHYME_DETAIL_ROUTE.withArgs(query)
    } else {
        route = Screen.DEFINITION_DETAIL_ROUTE.route
    }
    return route
}
