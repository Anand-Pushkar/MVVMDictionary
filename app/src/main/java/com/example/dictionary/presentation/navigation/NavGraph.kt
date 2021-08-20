package com.example.dictionary.presentation.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.dictionary.presentation.ui.definitionDetails.DefinitionDetailScreen
import com.example.dictionary.presentation.ui.home.HomeTabs
import com.example.dictionary.presentation.ui.home.home
import com.example.dictionary.presentation.ui.onboarding.Onboarding
import com.example.dictionary.presentation.ui.rhymeDetails.RhymeDetailScreen
import com.example.dictionary.presentation.ui.searchScreen.SearchScreen


@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun NavGraph(
    darkTheme: Boolean,
    modifier: Modifier = Modifier,
    onToggleTheme: () -> Unit,
    finishActivity: () -> Unit = {},
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.HOME_ROUTE.route,
    setOnboardingComplete: () -> Unit,
    onboardingComplete: State<Boolean>,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val actions = remember(navController) { MainActions(navController) }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(Screen.ONBOARDING_ROUTE.route) {
            // Intercept back in Onboarding: make it finish the activity
            BackHandler {
                finishActivity()
            }

            Onboarding(
                darkTheme = darkTheme,
                onboardingComplete = {
                    // Set the flag so that onboarding is not shown next time, flag is stored in dataStore.
                    setOnboardingComplete()
                    actions.onboardingComplete()
                }
            )
        }
        navigation(
            route = Screen.HOME_ROUTE.route,
            startDestination = navBackStackEntry?.destination?.route?: HomeTabs.DEFINITION.route
        ){
            home(
                darkTheme = darkTheme,
                navController = navController,
                modifier = modifier,
                onboardingComplete = onboardingComplete,
                onToggleTheme = { onToggleTheme() },
                onNavigateToDetailScreen = { route ->
                    navBackStackEntry?.let { actions.openDefinitionDetail(route, it) }
                },
                onNavigateToSearchScreen = { route ->
                    navBackStackEntry?.let { actions.openSearchScreen(route, it) }
                }
            )
        }
        composable(Screen.DEFINITION_DETAIL_ROUTE.route){ backStackEntry: NavBackStackEntry ->
            DefinitionDetailScreen(
                onNavigateToSearchScreen = { route ->
                    actions.openSearchScreen(route, backStackEntry)
                }
            )
        }
        composable(Screen.RHYME_DETAIL_ROUTE.route){ backStackEntry: NavBackStackEntry ->
            RhymeDetailScreen(
                onNavigateToSearchScreen = { route ->
                    actions.openSearchScreen(route, backStackEntry)
                }
            )
        }
        composable(Screen.SEARCH_SCREEN_ROUTE.route){ backStackEntry: NavBackStackEntry ->
            SearchScreen(
                onNavigateToDefinitionDetailScreen = { route ->
                    actions.openDefinitionDetail(route, backStackEntry)
                }
            )
        }
    }
}

class MainActions(navController: NavHostController) {

    val onboardingComplete: () -> Unit = {
        navController.popBackStack()
    }

    val openDefinitionDetail = { route: String, from: NavBackStackEntry ->
        // In order to discard duplicated navigation events, we check the Lifecycle
        if (from.lifecycleIsResumed()) {
            navController.navigate(route)
        }
    }

    val openSearchScreen = { route: String, from: NavBackStackEntry ->
        // In order to discard duplicated navigation events, we check the Lifecycle
        if (from.lifecycleIsResumed()) {
            navController.navigate(route)
        }
    }

}




/**
 * If the lifecycle is not resumed it means this NavBackStackEntry already processed a nav event.
 *
 * This is used to de-duplicate navigation events.
 */
fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED
