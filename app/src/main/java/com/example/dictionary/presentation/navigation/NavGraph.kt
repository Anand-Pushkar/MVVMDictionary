package com.example.dictionary.presentation.navigation

import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navArgument
import androidx.navigation.navigation
import com.example.dictionary.presentation.rememberAnimatedNavController
import com.example.dictionary.presentation.ui.definitionDetails.DefinitionDetailScreen
import com.example.dictionary.presentation.ui.definitionDetails.DefinitionDetailViewModel
import com.example.dictionary.presentation.ui.home.HomeTabs
import com.example.dictionary.presentation.ui.home.home
import com.example.dictionary.presentation.ui.onboarding.Onboarding
import com.example.dictionary.presentation.ui.rhymeDetails.RhymeDetailScreen
import com.example.dictionary.presentation.ui.rhymeDetails.RhymeDetailViewModel
import com.example.dictionary.presentation.ui.searchScreen.SearchScreen
import com.example.dictionary.presentation.ui.searchScreen.SearchViewModel
import com.example.dictionary.util.TAG

import com.google.accompanist.navigation.animation.AnimatedNavHost
//import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.animation.composable


@ExperimentalStdlibApi
@RequiresApi(Build.VERSION_CODES.N)
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@Composable
fun NavGraph(
    darkTheme: MutableState<Boolean>,
    isNetworkAvailable: MutableState<Boolean>,
    onToggleTheme: () -> Unit,
    finishActivity: () -> Unit = {},
    navController: NavHostController = rememberAnimatedNavController(),
    setOnboardingComplete: () -> Unit,
    onboardingComplete: MutableState<Boolean>,
    startDestination: String,
) {
    Log.d(TAG, "NavGraph: onboardingComplete = ${onboardingComplete.value}")
    BoxWithConstraints {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val actions = remember(navController) { MainActions(navController) }

        AnimatedNavHost(
            navController = navController,
            startDestination = startDestination
        ) {

            onBoardingScreen(
                isNetworkAvailable = isNetworkAvailable,
                navController = navController,
                finishActivity = finishActivity,
                setOnboardingComplete = setOnboardingComplete
            )

            navigation(
                route = Screen.HOME_ROUTE.route,
                startDestination = HomeTabs.DEFINITION.route
            ){
                home(
                    darkTheme = darkTheme,
                    isNetworkAvailable = isNetworkAvailable,
                    navController = navController,
                    onboardingComplete = onboardingComplete,
                    onToggleTheme = { onToggleTheme() },
                    onNavigateToSearchScreen = { route ->
                        navBackStackEntry?.let { actions.openSearchScreen(route, it) }
                    },
                )
            }

            definitionDetailScreen(
                darkTheme = darkTheme,
                isNetworkAvailable = isNetworkAvailable,
                navController = navController,
                width = constraints.maxWidth,
            )

            rhymeDetailScreen(
                darkTheme = darkTheme,
                isNetworkAvailable = isNetworkAvailable,
                navController = navController,
                width = constraints.maxWidth,
            )

            searchScreen(
                darkTheme = darkTheme,
                isNetworkAvailable = isNetworkAvailable,
                navController = navController,
                height = constraints.maxHeight,
                width = constraints.maxWidth
            )
        }
    }

}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
fun NavGraphBuilder.onBoardingScreen(
    isNetworkAvailable: MutableState<Boolean>,
    navController: NavHostController,
    finishActivity: () -> Unit = {},
    setOnboardingComplete: () -> Unit,
){
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
            isNetworkAvailable = isNetworkAvailable
        ) {
            // Set the flag so that onboarding is not shown next time, flag is stored in dataStore.
            Log.d(TAG, "onBoardingScreen: setting onboarding complete")
            setOnboardingComplete()
            navController.popBackStack()
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
fun NavGraphBuilder.definitionDetailScreen(
    darkTheme: MutableState<Boolean>,
    isNetworkAvailable: MutableState<Boolean>,
    navController: NavHostController,
    width: Int
){
    // will always enter from the search screen for now (favorite word not yet designed)
    // enter = slide in
    composable(
        route = Screen.DEFINITION_DETAIL_ROUTE.route + "/{dQuery}",
        enterTransition = { _, _ ->
            slideInHorizontally(
                initialOffsetX = { width },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ) + fadeIn(animationSpec = tween(300))
        },
        popExitTransition = { _, target ->
            slideOutHorizontally(
                targetOffsetX = { width },
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
        },
        arguments = listOf(navArgument("dQuery"){
            type = NavType.StringType
        }),
    ){ backStackEntry: NavBackStackEntry ->

        backStackEntry.arguments?.getString("dQuery")?.let { query ->

            val factory = HiltViewModelFactory(
                LocalContext.current, backStackEntry
            )
            val viewModel: DefinitionDetailViewModel = viewModel(
                key = "DefinitionDetailViewModel",
                factory = factory
            )
            DefinitionDetailScreen(
                isDark = darkTheme,
                isNetworkAvailable = isNetworkAvailable,
                viewModel = viewModel,
                onNavigateToSearchScreen = { route ->
                    // In order to discard duplicated navigation events, we check the Lifecycle
                    if (backStackEntry.lifecycleIsResumed()) {
                        navController.navigate(route)
                    }
                },
                query = query
            )
        }

    }
}

@ExperimentalStdlibApi
@RequiresApi(Build.VERSION_CODES.N)
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
fun NavGraphBuilder.rhymeDetailScreen(
    darkTheme: MutableState<Boolean>,
    isNetworkAvailable: MutableState<Boolean>,
    navController: NavHostController,
    width: Int
){
    composable(
        route = Screen.RHYME_DETAIL_ROUTE.route + "/{rQuery}",
        enterTransition = { _, _ ->
            slideInHorizontally(
                initialOffsetX = { width },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ) + fadeIn(animationSpec = tween(300))
        },
        popExitTransition = { _, target ->
            slideOutHorizontally(
                targetOffsetX = { width },
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
        },
        arguments = listOf(navArgument("rQuery"){
            type = NavType.StringType
        }),
    ){ backStackEntry: NavBackStackEntry ->

        backStackEntry.arguments?.getString("rQuery")?.let { query ->

            val factory = HiltViewModelFactory(
                LocalContext.current, backStackEntry
            )
            val viewModel: RhymeDetailViewModel = viewModel(
                key = "RhymeDetailViewModel",
                factory = factory
            )
            RhymeDetailScreen(
                isDark = darkTheme,
                isNetworkAvailable = isNetworkAvailable,
                viewModel = viewModel,
                onNavigateToSearchScreen = { route ->
                    // In order to discard duplicated navigation events, we check the Lifecycle
                    if (backStackEntry.lifecycleIsResumed()) {
                        navController.navigate(route)
                    }
                },
                onNavigationToDefinitionDetailScreen = { route ->
                    // In order to discard duplicated navigation events, we check the Lifecycle
                    if (backStackEntry.lifecycleIsResumed()) {
                        navController.navigate(route)
                    }
                },
                query = query
            )
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
fun NavGraphBuilder.searchScreen(
    darkTheme: MutableState<Boolean>,
    isNetworkAvailable: MutableState<Boolean>,
    navController: NavHostController,
    height: Int,
    width: Int,

){
    // enter up, exit(going to detail screen) down, pop enter(back from detail screen) slide in from right, pop exit down
    composable(
        route = Screen.SEARCH_SCREEN_ROUTE.route + "/{parentScreen}",
        enterTransition = { _, _ ->
            slideInVertically(
                initialOffsetY = { height },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ) + fadeIn(animationSpec = tween(300))
        },
        popExitTransition = { _, target ->
            slideOutVertically(
                targetOffsetY = { height },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ) + fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = { _, _ ->
            slideInHorizontally(
                initialOffsetX = { -width },
                animationSpec = tween(
                    durationMillis = 300,
                    easing = FastOutSlowInEasing
                )
            ) + fadeIn(animationSpec = tween(300))
        },
        exitTransition = { _, _ ->
            slideOutHorizontally(
                targetOffsetX = { -width },
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
                    // In order to discard duplicated navigation events, we check the Lifecycle
                    if (backStackEntry.lifecycleIsResumed()) {
                        navController.navigate(route)
                    }
                },
                parent = mutableStateOf(it),
                viewModel = viewModel
            )
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
