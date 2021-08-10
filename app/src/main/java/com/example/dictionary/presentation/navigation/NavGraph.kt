package com.example.dictionary.presentation.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
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
                    navController.popBackStack()
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
                onNavigateToDetailScreen = {
                    if(navBackStackEntry?.lifecycleIsResumed() == true){
                        navController.navigate(it)
                    }
                }
            )
        }
        composable(Screen.DEFINITION_DETAIL_ROUTE.route){
            DefinitionDetailScreen()
        }
        composable(Screen.RHYME_DETAIL_ROUTE.route){
            RhymeDetailScreen()
        }

    }
}



/**
 * If the lifecycle is not resumed it means this NavBackStackEntry already processed a nav event.
 *
 * This is used to de-duplicate navigation events.
 */
private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED
