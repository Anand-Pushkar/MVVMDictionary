package com.example.dictionary.presentation.ui.searchScreen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.dictionary.presentation.navigation.Screen
import com.example.dictionary.util.TAG


val list = listOf("banana", "apple", "mango", "peach", "watermelon", "guava", "pineapple", "orange", "kiwi", "avocado",
    "banana", "apple", "mango", "peach", "watermelon", "guava", "pineapple", "orange", "kiwi", "avocado")

@ExperimentalComposeUiApi
@Composable
fun SearchScreen(
    onNavigateToDefinitionDetailScreen: (String) -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        backgroundColor = MaterialTheme.colors.primary,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp, bottom = 48.dp)
        ) {
            SearchSection(
                onNavigateToDefinitionDetailScreen = onNavigateToDefinitionDetailScreen
            )
            SearchSuggestionsList(
                onNavigateToDefinitionDetailScreen = onNavigateToDefinitionDetailScreen
            )
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun SearchSection(
    onNavigateToDefinitionDetailScreen: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        modifier = Modifier
            .fillMaxWidth(),
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
            Icon(
                Icons.Filled.Search,
                contentDescription = "Search Icon",
                tint = MaterialTheme.colors.onPrimary
            )
        },
        keyboardActions = KeyboardActions(
            onDone = {
                //onExecuteSearch()
                val route = Screen.DEFINITION_DETAIL_ROUTE.route
                onNavigateToDefinitionDetailScreen(route)
                keyboardController?.hide()
            }
        ),
        textStyle = TextStyle(color = MaterialTheme.colors.onPrimary),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.primary,
            focusedIndicatorColor = MaterialTheme.colors.secondary,
//                unfocusedIndicatorColor = Color.Transparent,
//                disabledIndicatorColor = Color.Transparent
        ),
    )
}

@Composable
fun SearchSuggestionsList(
    onNavigateToDefinitionDetailScreen: (String) -> Unit,
){
    LazyColumn(
        modifier = Modifier
            .padding(top = 16.dp),
    ) {
        itemsIndexed(
            items = list
        ){ index: Int, item: String ->
            Text(
                text = item,
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
                            val route = Screen.DEFINITION_DETAIL_ROUTE.route
                            onNavigateToDefinitionDetailScreen(route)
                            Log.d(TAG, "SearchSuggestionsList: ${list.size}")
                        }
                    )
            )
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun SearchSection1() {
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(1.5.dp),
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
            Icon(
                Icons.Filled.Search,
                contentDescription = "Search Icon",
                tint = MaterialTheme.colors.onPrimary
            )
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
        shape = MaterialTheme.shapes.small
            .copy(
                topStart = CornerSize(32.dp),
                topEnd = CornerSize(32.dp),
                bottomEnd = CornerSize(32.dp),
                bottomStart = CornerSize(32.dp)
            )
    )
}