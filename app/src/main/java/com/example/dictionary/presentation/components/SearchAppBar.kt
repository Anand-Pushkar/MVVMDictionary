package com.example.dictionary.presentation.components

import android.util.Log
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.test.isFocused
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.dictionary.presentation.navigation.Screen
import com.example.dictionary.util.TAG

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun SearchAppBar(
    onNavigateToSearchScreen: (String) -> Unit,
    //query: String,
    //onQueryChanged: (String) -> Unit,
    //onExecuteSearch: () -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clip(RoundedCornerShape(32.dp)),
        color = MaterialTheme.colors.secondary,
        elevation = 8.dp,
        onClick = {
            val route = Screen.SEARCH_SCREEN_ROUTE.route
            onNavigateToSearchScreen(route)
        },
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(1.5.dp)
                .onFocusEvent {
                    if(it.isFocused){
                        Log.d(TAG, "SearchAppBar1: ######### ${Screen.SEARCH_SCREEN_ROUTE.route}")
                        val route = Screen.SEARCH_SCREEN_ROUTE.route
                        onNavigateToSearchScreen(route)
                    }
                },
            value = "",
            onValueChange = {
                //onQueryChanged(it)
            },
            label = {
                Text(text = "Search", color = MaterialTheme.colors.onPrimary)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
            ),
            leadingIcon = {
                Icon(Icons.Filled.Search, contentDescription = "Search Icon", tint = MaterialTheme.colors.onPrimary)
            },
            keyboardActions = KeyboardActions(
                onDone = {
                    //onExecuteSearch()
                    keyboardController?.hide()
                }
            ),
            textStyle = TextStyle(color = MaterialTheme.colors.onPrimary),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.primary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = MaterialTheme.shapes.small.copy(
                topStart = CornerSize(32.dp),
                topEnd = CornerSize(32.dp),
                bottomEnd = CornerSize(32.dp),
                bottomStart = CornerSize(32.dp)
            )
        )
    }
}

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun SearchAppBar1(
    onNavigateToSearchScreen: (String) -> Unit,
    //query: String,
    //onQueryChanged: (String) -> Unit,
    //onExecuteSearch: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp, start = 16.dp, end = 16.dp)
                .onFocusEvent {
                    if(it.isFocused){
                        Log.d(TAG, "SearchAppBar1: ######### ${Screen.SEARCH_SCREEN_ROUTE.route}")
                        val route = Screen.SEARCH_SCREEN_ROUTE.route
                        onNavigateToSearchScreen(route)
                    }
                },
            value = "Search",
            onValueChange = {
                //onQueryChanged(it)
            },
//        label = {
//            Text(text = "Search", color = MaterialTheme.colors.onPrimary)
//        },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done,
            ),
            leadingIcon = {
                Icon(Icons.Filled.Search, contentDescription = "Search Icon", tint = MaterialTheme.colors.onPrimary)
            },
            keyboardActions = KeyboardActions(
                onDone = {
                    //onExecuteSearch()
                    keyboardController?.hide()
                }
            ),
            textStyle = TextStyle(color = MaterialTheme.colors.onPrimary),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.primary,
                focusedIndicatorColor = MaterialTheme.colors.secondary,
                unfocusedIndicatorColor = MaterialTheme.colors.secondary,
//            disabledIndicatorColor = Color.Transparent
            ),
            shape = MaterialTheme.shapes.small.copy(
                topStart = CornerSize(32.dp),
                topEnd = CornerSize(32.dp),
                bottomEnd = CornerSize(32.dp),
                bottomStart = CornerSize(32.dp)
            ),
        )

    }
}

