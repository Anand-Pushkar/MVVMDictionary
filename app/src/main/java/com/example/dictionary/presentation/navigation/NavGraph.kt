package com.example.dictionary.presentation.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navArgument
import androidx.navigation.navigation
import com.example.dictionary.presentation.rememberAnimatedNavController
import com.example.dictionary.presentation.ui.definitionDetails.DefinitionDetailScreen
import com.example.dictionary.presentation.ui.home.HomeTabs
import com.example.dictionary.presentation.ui.home.home
import com.example.dictionary.presentation.ui.onboarding.Onboarding
import com.example.dictionary.presentation.ui.rhymeDetails.RhymeDetailScreen
import com.example.dictionary.presentation.ui.searchScreen.SearchScreen
import com.example.dictionary.presentation.ui.searchScreen.SearchViewModel

import com.google.accompanist.navigation.animation.AnimatedNavHost
//import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.navigation.animation.composable


@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun NavGraph(
    darkTheme: MutableState<Boolean>,
    isNetworkAvailable: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    onToggleTheme: () -> Unit,
    finishActivity: () -> Unit = {},
    navController: NavHostController = rememberAnimatedNavController(),
    startDestination: String = Screen.HOME_ROUTE.route,
    setOnboardingComplete: () -> Unit,
    onboardingComplete: State<Boolean>,
) {
    BoxWithConstraints {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val actions = remember(navController) { MainActions(navController) }

        AnimatedNavHost(
            navController = navController,
            startDestination = startDestination
        ) {

            composable(
                route = Screen.ONBOARDING_ROUTE.route,
                exitTransition = { _, _ ->
                    fadeOut(animationSpec = tween(300))
                },
            ) {
                // Intercept back in Onboarding: make it finish the activity
                BackHandler {
                    finishActivity()
                }

                Onboarding(
                    darkTheme = darkTheme.value,
                    isNetworkAvailable = isNetworkAvailable,
                    onboardingComplete = {
                        // Set the flag so that onboarding is not shown next time, flag is stored in dataStore.
                        setOnboardingComplete()
                        actions.onboardingComplete()
                    }
                )
            }
            navigation(
                route = Screen.HOME_ROUTE.route,
                startDestination = HomeTabs.DEFINITION.route
            ){
                home(
                    darkTheme = darkTheme,
                    isNetworkAvailable = isNetworkAvailable,
                    navController = navController,
                    modifier = modifier,
                    onboardingComplete = onboardingComplete,
                    onToggleTheme = { onToggleTheme() },
                    onNavigateToDetailScreen = { route ->
                        navBackStackEntry?.let { actions.openDetailScreen(route, it) }
                    },

                    onNavigateToSearchScreen = { route ->
                        navBackStackEntry?.let { actions.openSearchScreen(route, it) }
                    },
                    width = constraints.maxWidth
                )
            }

            // will always enter from the search screen for now (favorite word not yet designed)
            // enter = slide in
            composable(
                route = Screen.DEFINITION_DETAIL_ROUTE.route,
                enterTransition = { _, _ ->
                    slideInHorizontally(
                        initialOffsetX = { constraints.maxWidth },
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    ) + fadeIn(animationSpec = tween(300))
                },
                popExitTransition = { _, target ->
                    slideOutHorizontally(
                        targetOffsetX = { constraints.maxWidth },
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    ) + fadeOut(animationSpec = tween(300))
                },
                exitTransition = { _, _ ->
                    fadeOut(animationSpec = tween(300))
                },
                popEnterTransition = { _, _ ->
                    fadeIn(animationSpec = tween(300))
                }
            ){ backStackEntry: NavBackStackEntry ->
                DefinitionDetailScreen(
                    isDark = darkTheme,
                    isNetworkAvailable = isNetworkAvailable,
                    onNavigateToSearchScreen = { route ->
                        actions.openSearchScreen(route, backStackEntry)
                    }
                )
            }

            composable(
                route = Screen.RHYME_DETAIL_ROUTE.route,
                enterTransition = { _, _ ->
                    slideInHorizontally(
                        initialOffsetX = { constraints.maxWidth },
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    ) + fadeIn(animationSpec = tween(300))
                },
                popExitTransition = { _, target ->
                    slideOutHorizontally(
                        targetOffsetX = { constraints.maxWidth },
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    ) + fadeOut(animationSpec = tween(300))
                },
                exitTransition = { _, _ ->
                    fadeOut(animationSpec = tween(300))
                },
                popEnterTransition = { _, _ ->
                    fadeIn(animationSpec = tween(300))
                }
            ){ backStackEntry: NavBackStackEntry ->
                RhymeDetailScreen(
                    isDark = darkTheme,
                    isNetworkAvailable = isNetworkAvailable,
                    onNavigateToSearchScreen = { route ->
                        actions.openSearchScreen(route, backStackEntry)
                    }
                )
            }

            // enter up, exit(going to detail screen) down, pop enter(back from detail screen) slide in from right, pop exit down
            composable(
                route = Screen.SEARCH_SCREEN_ROUTE.route + "/{parentScreen}",
                enterTransition = { _, _ ->
                    slideInVertically(
                        initialOffsetY = { constraints.maxHeight },
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    ) + fadeIn(animationSpec = tween(300))
                },
                popExitTransition = { _, target ->
                    slideOutVertically(
                        targetOffsetY = { constraints.maxHeight },
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    ) + fadeOut(animationSpec = tween(300))
                },
                popEnterTransition = { _, _ ->
                    slideInHorizontally(
                        initialOffsetX = { -constraints.maxWidth },
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    ) + fadeIn(animationSpec = tween(300))
                },
                exitTransition = { _, _ ->
                    slideOutHorizontally(
                        targetOffsetX = { -constraints.maxWidth },
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = FastOutSlowInEasing
                        )
                    ) + fadeOut(animationSpec = tween(300))
                },
                arguments = listOf(navArgument("parentScreen"){
                    type = NavType.StringType
                }),
            ){ backStackEntry: NavBackStackEntry ->
                backStackEntry.arguments?.getString("parentScreen")?.let {

                    val factory = HiltViewModelFactory(
                        LocalContext.current, backStackEntry
                    )
                    val viewModel: SearchViewModel = viewModel(
                        key = "SearchViewModel",
                        factory = factory
                    )
                    SearchScreen(
                        isDark = darkTheme,
                        isNetworkAvailable = isNetworkAvailable,
                        onNavigateToDetailScreen = { route ->
                            actions.openDetailScreen(route, backStackEntry)
                        },
                        parent = mutableStateOf(it),
                        viewModel = viewModel
                    )
                }
            }
        }
    }

}

class MainActions(navController: NavHostController) {

    val onboardingComplete: () -> Unit = {
        navController.popBackStack()
    }

    val openDetailScreen = { route: String, from: NavBackStackEntry ->
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
