package com.example.dictionary.presentation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.plusAssign
import com.example.dictionary.presentation.components.bottomNavigationComponent.DictionaryBottomBar
import com.example.dictionary.presentation.navigation.NavGraph
import com.example.dictionary.presentation.theme.TabTheme
import com.example.dictionary.presentation.ui.home.HomeTabs
import com.example.dictionary.presentation.ui.util.DialogQueue
import com.example.dictionary.presentation.util.currentTabRoute
import com.example.dictionary.presentation.util.currentTab
import com.example.dictionary.util.TAG
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.navigation.animation.AnimatedComposeNavigator

//import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.animation.navigation


@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun DictionaryApp(
    isDarkTheme: MutableState<Boolean>,
    isNetworkAvailable: MutableState<Boolean>,
    onToggleTheme: () -> Unit,
    finishActivity: () -> Unit,
    setOnboardingComplete: () -> Unit,
    onboardingComplete: State<Boolean>,
) {
    ProvideWindowInsets {
        val tabs = remember { HomeTabs.values() }
        val navController = rememberAnimatedNavController()
        val scaffoldState = rememberScaffoldState()
        val dialogQueue = DialogQueue()

        val currentRoute = currentTabRoute(navController = navController)
        val currentTab = currentTab(currentRoute = currentRoute, tabs = tabs)

        TabTheme(
            isDarkTheme = isDarkTheme,
            isNetworkAvailable = mutableStateOf(true), // we don't want to show multiple "no internet" messages
            scaffoldState = scaffoldState,
            dialogQueue = dialogQueue.queue.value, // replace with the reference created in the viewModel
            displayProgressBar = false, // replace with loading
            selectedTab = currentTab
        ) {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize(),
                backgroundColor = MaterialTheme.colors.primary,
                bottomBar = { DictionaryBottomBar(navController = navController, tabs = tabs) },
                scaffoldState = scaffoldState,
                snackbarHost = {
                    scaffoldState.snackbarHostState
                },
            ) {
                NavGraph(
                    darkTheme = isDarkTheme,
                    isNetworkAvailable = isNetworkAvailable,
                    finishActivity = finishActivity,
                    onToggleTheme = { onToggleTheme() },
                    navController = navController,
                    modifier = Modifier.padding(0.dp),
                    setOnboardingComplete = setOnboardingComplete,
                    onboardingComplete = onboardingComplete,
                )
            }
        }
    }
}

@SuppressLint("RememberReturnType")
@ExperimentalAnimationApi
@Composable
public fun rememberAnimatedNavController(): NavHostController {
    val navController = rememberNavController()
    val animatedNavigator = remember(navController) { AnimatedComposeNavigator() }
    return navController.apply {
        navigatorProvider += animatedNavigator
    }
}







