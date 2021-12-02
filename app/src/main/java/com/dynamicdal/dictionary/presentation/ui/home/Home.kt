package com.dynamicdal.dictionary.presentation.ui.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.MutableState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.dynamicdal.dictionary.R
import com.dynamicdal.dictionary.presentation.ui.home.definition.DefinitionScreen
import com.dynamicdal.dictionary.presentation.ui.home.rhyme.RhymeScreen
import com.google.accompanist.navigation.animation.composable

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
fun NavGraphBuilder.home(
    darkTheme: MutableState<Boolean>,
    isNetworkAvailable: MutableState<Boolean>,
    navController: NavHostController,
    onboardingComplete: MutableState<Boolean>,
    onToggleTheme: () -> Unit,
    onNavigateToSearchScreen: (String) -> Unit,
    onNavigateToFavoriteScreen: (String) -> Unit,
    userName: MutableState<String>,
) {
    composable(
        route = HomeTabs.DEFINITION.route,
        enterTransition = { _, _ ->
            fadeIn(animationSpec = tween(300))
        },
        exitTransition = { _, _ ->
            fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = { _, _ ->
            fadeIn(animationSpec = tween(300))
        }
    ) {

        if (onboardingComplete.value) { // Avoid glitch when showing onboarding
            DefinitionScreen(
                darkTheme = darkTheme,
                isNetworkAvailable = isNetworkAvailable,
                onToggleTheme = { onToggleTheme() },
                onNavigateToSearchScreen = onNavigateToSearchScreen,
                onNavigateToMyWordsScreen = onNavigateToFavoriteScreen,
                navController = navController,
                userName = userName
            )
        }
    }
    composable(
        route = HomeTabs.RHYME.route,
        enterTransition = { _, _ ->
            fadeIn(animationSpec = tween(300))
        },
        exitTransition = { _, _ ->
            fadeOut(animationSpec = tween(300))
        },
        popEnterTransition = { _, _ ->
            fadeIn(animationSpec = tween(300))
        }
    ) {
        RhymeScreen(
            darkTheme = darkTheme,
            isNetworkAvailable = isNetworkAvailable,
            onToggleTheme = { onToggleTheme() },
            onNavigateToSearchScreen = onNavigateToSearchScreen,
            onNavigateToMyRhymesScreen = onNavigateToFavoriteScreen,
            navController = navController,
            userName = userName
        )
    }
}

enum class HomeTabs(
    val id: String,
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val route: String
) {
    DEFINITION("definition", R.string.definition, R.drawable.ic_book_white, HomeTabDestinations.DEFINITION_ROUTE),
    RHYME("rhyme", R.string.rhyme, R.drawable.ic_waves_white, HomeTabDestinations.RHYME_ROUTE),
}

/**
 * Destinations used in the home screen of the ([DictionaryApp]).
 */
private object HomeTabDestinations {
    const val DEFINITION_ROUTE = "home/definition"
    const val RHYME_ROUTE = "home/rhyme"
}
