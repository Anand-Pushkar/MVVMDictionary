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
import androidx.navigation.NavController
import com.example.dictionary.presentation.ui.home.HomeTabs
import com.example.dictionary.presentation.util.currentRoute
import com.example.dictionary.util.TAG
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.navigationBarsPadding
import java.util.*

@Composable
fun DictionaryBottomBar(navController: NavController, tabs: Array<HomeTabs>) {

    val routes = remember { HomeTabs.values().map { it.route } }
    val currentRoute = currentRoute(navController = navController)

    Log.d(TAG, "DictionaryBottomBar: $currentRoute")
    if (currentRoute in routes) {
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
                    selected = currentRoute == tab.route,
                    onClick = {
                        if (tab.route != currentRoute) {
                            navController.navigate(tab.route) {
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
