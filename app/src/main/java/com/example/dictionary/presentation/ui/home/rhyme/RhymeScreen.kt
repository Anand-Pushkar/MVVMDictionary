package com.example.dictionary.presentation.ui.home.rhyme

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.dictionary.presentation.components.FavouriteCard
import com.example.dictionary.presentation.components.GreetingSection
import com.example.dictionary.presentation.components.SearchAppBar
import com.example.dictionary.presentation.components.bottomNavigationComponent.DictionaryBottomBar
import com.example.dictionary.presentation.navigation.Screen
import com.example.dictionary.presentation.theme.YellowTheme
import com.example.dictionary.presentation.ui.home.HomeTabs
import com.example.dictionary.presentation.ui.util.DialogQueue
import com.example.dictionary.util.RHYME

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun RhymeScreen(
    darkTheme: MutableState<Boolean>,
    isNetworkAvailable: MutableState<Boolean>,
    onToggleTheme: () -> Unit,
    onNavigateToSearchScreen: (String) -> Unit,
    navController: NavHostController,
) {
    val scaffoldState = rememberScaffoldState()

    YellowTheme(
        darkTheme = darkTheme,
        isNetworkAvailable = isNetworkAvailable,
        scaffoldState = scaffoldState,
        dialogQueue = DialogQueue().queue.value, // replace with the reference created in the viewModel
        displayProgressBar = false, // replace with loading
    ) {
        val tabs = remember { HomeTabs.values() }
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            backgroundColor = MaterialTheme.colors.primary,
            bottomBar = { DictionaryBottomBar(navController = navController, tabs = tabs) },
            topBar = {
                GreetingSection(
                    isNetworkAvailable = isNetworkAvailable,
                    isDarkTheme = darkTheme,
                    onToggleTheme = { onToggleTheme() })
            },
            scaffoldState = scaffoldState,
            snackbarHost = {
                scaffoldState.snackbarHostState
            },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                SearchAppBar(
                    onNavigateToSearchScreen = onNavigateToSearchScreen,
                    route = Screen.SEARCH_SCREEN_ROUTE.withArgs(RHYME)
                )
                FavouriteCard(
                    color = MaterialTheme.colors.primaryVariant,
                    mainText = "My Rhymes",
                    secondaryText = "Favourite words show here",
                )
            }
        }
    }
}