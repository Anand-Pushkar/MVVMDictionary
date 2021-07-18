package com.example.dictionary.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.dictionary.presentation.components.bottomNavigationComponent.DictionaryBottomBar
import com.example.dictionary.presentation.navigation.NavGraph
import com.example.dictionary.presentation.theme.TabTheme
import com.example.dictionary.presentation.ui.home.HomeTabs
import com.example.dictionary.presentation.util.currentRoute
import com.example.dictionary.presentation.util.currentTab
import com.google.accompanist.insets.ProvideWindowInsets


@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun DictionaryApp(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    finishActivity: () -> Unit,
    setOnboardingComplete: () -> Unit,
    onboardingComplete: State<Boolean>,
) {
    ProvideWindowInsets {
        val tabs = remember { HomeTabs.values() }
        val navController = rememberNavController()

        val currentRoute = currentRoute(navController = navController)
        val currentTab = currentTab(currentRoute = currentRoute, tabs = tabs)

        TabTheme(
            isDarkTheme = isDarkTheme,
            selectedTab = currentTab
        ) {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize(),
                backgroundColor = MaterialTheme.colors.primary,
                bottomBar = { DictionaryBottomBar(navController = navController, tabs = tabs) }
            ) {
                NavGraph(
                    darkTheme = isDarkTheme,
                    finishActivity = finishActivity,
                    onToggleTheme = { onToggleTheme() },
                    navController = navController,
                    modifier = Modifier.padding(it),
                    setOnboardingComplete = setOnboardingComplete,
                    onboardingComplete = onboardingComplete
                )
            }
        }
    }
}






