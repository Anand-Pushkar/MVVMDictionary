package com.dynamicdal.dictionary.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun SearchAppBar(
    onNavigateToSearchScreen: (String) -> Unit,
    route: String,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clip(RoundedCornerShape(32.dp)),
        color = MaterialTheme.colors.secondary,
        elevation = 16.dp,

    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)
                .onFocusEvent {
                    if(it.isFocused){
                        keyboardController?.hide()
                        onNavigateToSearchScreen(route)
                    }
                },
            value = "",
            onValueChange = {
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

