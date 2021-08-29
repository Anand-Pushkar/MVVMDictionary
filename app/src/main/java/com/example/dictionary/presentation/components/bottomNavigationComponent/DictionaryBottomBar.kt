package com.example.dictionary.presentation.components.bottomNavigationComponent

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dictionary.presentation.ui.home.HomeTabs
import com.example.dictionary.presentation.util.currentTabRoute
import com.example.dictionary.util.TAG
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.navigationBarsPadding
import java.util.*

@Composable
fun DictionaryBottomBar(
    navController: NavHostController,
    tabs: Array<HomeTabs>
) {

    val routes = remember { HomeTabs.values().map { it.route } }
    val currentRoute = currentTabRoute(navController = navController)

    Log.d(TAG, "DictionaryBottomBar: $currentRoute")
    if (currentRoute.value in routes) {
        BottomNavigation(
            modifier = Modifier
                .navigationBarsHeight(additional = 60.dp),
            backgroundColor = MaterialTheme.colors.surface,
            elevation = 4.dp
        ) {
            tabs.forEach { tab ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            modifier = Modifier
                                .padding(bottom = 4.dp),
                            painter = painterResource(tab.icon),
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(tab.title).uppercase(Locale.getDefault()),
                            style = MaterialTheme.typography.h5
                        )
                    },
                    selected = currentRoute.value == tab.route,
                    onClick = {
                        if (tab.route != currentRoute.value) {
                            navController.navigate(tab.route) {
                                Log.d(TAG, "DictionaryBottomBar: ${navController.graph.startDestinationId}")
                                Log.d(TAG, "DictionaryBottomBar: ${navController.graph.nodes}")
                                 popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    alwaysShowLabel = false,
                    selectedContentColor = MaterialTheme.colors.secondaryVariant,
                    //unselectedContentColor = LocalContentColor.current,
                    unselectedContentColor = MaterialTheme.colors.onSurface,
                    modifier = Modifier.navigationBarsPadding()
                )
            }
        }
    }
}
