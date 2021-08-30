package com.example.dictionary.presentation.ui.searchScreen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
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

    val query = viewModel.query.value
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
        displayProgressBar = loading, // replace with loading
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

            val route = getRoute(parent = parent)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 48.dp, bottom = 48.dp)
            ) {
                SearchSection(
                    onNavigateToDetailScreen = onNavigateToDetailScreen,
                    route = route,
                    query = query,
                    textFieldValue = textFieldValue,
                    onQueryChanged = {
                        viewModel.onTriggerEvent(SearchScreenEvent.OnQueryChangedEvent(it))
                    },
                    onTextFieldValueChanged = {
                        viewModel.onTriggerEvent(SearchScreenEvent.OnTextFieldValueChanged(it))
                    }
                )
                SearchSuggestionsList(
                    loading = loading,
                    onNavigateToDetailScreen = onNavigateToDetailScreen,
                    route = route,
                    searchSuggestions = searchSuggestions,
                    onQueryChanged = {
                        viewModel.onTriggerEvent(SearchScreenEvent.OnQueryChangedEvent(it))
                    },
                    onTextFieldValueChanged = {
                        viewModel.onTriggerEvent(SearchScreenEvent.OnTextFieldValueChanged(it))
                    }
                )
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun SearchSection(
    onNavigateToDetailScreen: (String) -> Unit,
    route: String,
    query: String,
    textFieldValue: TextFieldValue,
    onQueryChanged: (String) -> Unit,
    onTextFieldValueChanged: (TextFieldValue) -> Unit,
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
            Log.d(TAG, "SearchSection: ${it.text}")
            onQueryChanged(it.text)
            onTextFieldValueChanged(it)
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
                Icons.Filled.Search,
                contentDescription = "Search Icon",
                tint = MaterialTheme.colors.onPrimary
            )
        },
        keyboardActions = KeyboardActions(
            onDone = {
                //onExecuteSearch()
                onNavigateToDetailScreen(route)
                keyboardController?.hide()
            }
        ),
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
    route: String,
    searchSuggestions: List<SearchSuggestion>,
) {
    val scrollState = rememberLazyListState()
    if(loading){
        // maybe show shimmer animation but for now our theme will show a progress bar
    }
    else{
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
                        .clickable(onClick = { /* todo */ })
                        .padding(
                            start = 16.dp,
                            top = 8.dp,
                            end = 16.dp,
                            bottom = 8.dp
                        )
                        .wrapContentWidth(Alignment.Start)
                        .selectable(
                            selected = true,
                            onClick = {
                                onQueryChanged(item.word) // this update the query
                                onTextFieldValueChanged(TextFieldValue().copy(
                                    text = item.word,
                                    selection = TextRange(item.word.length)
                                ))
                                // pass the selected word with the route
                                onNavigateToDetailScreen(route)
                            }
                        )
                )
            }
        }
    }

}

@Composable
private fun getRoute(
    parent: MutableState<String>
): String {
    val route: String
    if (parent.value == "definition") {
        route = Screen.DEFINITION_DETAIL_ROUTE.route
    } else if (parent.value == "rhyme") {
        route = Screen.RHYME_DETAIL_ROUTE.route
    } else {
        route = Screen.DEFINITION_DETAIL_ROUTE.route
    }
    return route
}
