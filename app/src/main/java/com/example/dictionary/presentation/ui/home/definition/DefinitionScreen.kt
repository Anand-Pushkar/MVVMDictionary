package com.example.dictionary.presentation.ui.home.definition

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.dictionary.presentation.components.FavouriteCard
import com.example.dictionary.presentation.components.GreetingSection
import com.example.dictionary.presentation.components.SearchAppBar
import com.example.dictionary.presentation.navigation.Screen
import com.example.dictionary.presentation.theme.DictionaryTheme


@ExperimentalComposeUiApi
@Composable
fun DefinitionScreen(
    darkTheme: Boolean,
    modifier: Modifier,
    onToggleTheme: () -> Unit,
    isSearchOn: MutableState<Boolean> = mutableStateOf(false),
    onNavigateToDefinitionDetailScreen: (String) -> Unit
) {

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        backgroundColor = MaterialTheme.colors.primary,
        topBar = {
            if (isSearchOn.value) {
                SearchAppBar()
            } else {
                GreetingSection(
                    isDarkTheme = darkTheme,
                    onToggleTheme = { onToggleTheme() })
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            SearchAppBar()
            FavouriteCard(
                color = MaterialTheme.colors.primaryVariant,
                mainText = "My Words",
                secondaryText = "Favourite words show here",
            )

            Button(
                modifier = modifier
                    .padding(start = 120.dp, top = 120.dp),
                onClick = {
                    val route = Screen.DEFINITION_DETAIL_ROUTE.route
                    onNavigateToDefinitionDetailScreen(route)
                },
                enabled = true,
                elevation = ButtonDefaults.elevation(8.dp),
                colors = ButtonDefaults
                    .buttonColors(
                        backgroundColor = MaterialTheme.colors.primaryVariant,
                        contentColor = MaterialTheme.colors.onPrimary
                    ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(text = "Search On/Off")
            }
        }
    }

//    Column(
//        modifier = modifier
//            .fillMaxSize()
//    ) {
//        GreetingSection(
//            isDarkTheme = darkTheme,
//            onToggleTheme = { onToggleTheme() }
//        )
//        SearchAppBar()
//        FavouriteCard(
//            color = MaterialTheme.colors.primaryVariant,
//            mainText = "My Words",
//            secondaryText = "Favourite words show here",
//        )
//    }
}