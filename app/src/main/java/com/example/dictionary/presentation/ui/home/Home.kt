package com.example.dictionary.presentation.ui.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.dictionary.R

fun NavGraphBuilder.home(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    composable(HomeTabs.DEFINITION.route) { from ->

    }
    composable(HomeTabs.RHYME.route) { from ->

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
