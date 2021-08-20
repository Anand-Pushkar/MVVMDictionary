package com.example.dictionary.presentation.ui.home

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.dictionary.R
import com.example.dictionary.presentation.navigation.Screen
import com.example.dictionary.presentation.ui.home.definition.DefinitionScreen
import com.example.dictionary.presentation.ui.home.rhyme.RhymeScreen
import com.example.dictionary.util.TAG

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
fun NavGraphBuilder.home(
    darkTheme: Boolean,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onboardingComplete: State<Boolean>,
    onToggleTheme: () -> Unit,
    onNavigateToDetailScreen: (String) -> Unit,
    onNavigateToSearchScreen: (String) -> Unit,
) {
    composable(HomeTabs.DEFINITION.route) {
        // Show onboarding instead if not shown yet.
        LaunchedEffect(onboardingComplete) {
            if (!onboardingComplete.value) {
                Log.d(TAG, "home: hello, going to onboarding route")
                navController.navigate(Screen.ONBOARDING_ROUTE.route)
            }
        }
        if (onboardingComplete.value) { // Avoid glitch when showing onboarding
            DefinitionScreen(
                darkTheme = darkTheme,
                modifier = modifier,
                onToggleTheme = { onToggleTheme() },
                onNavigateToDefinitionDetailScreen = onNavigateToDetailScreen,
                onNavigateToSearchScreen = onNavigateToSearchScreen
            )
        }
    }
    composable(HomeTabs.RHYME.route) {
        RhymeScreen(
            darkTheme = darkTheme,
            modifier = modifier,
            onToggleTheme = { onToggleTheme() },
            onNavigateToRhymeDetailScreen = onNavigateToDetailScreen,
            onNavigateToSearchScreen = onNavigateToSearchScreen
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
