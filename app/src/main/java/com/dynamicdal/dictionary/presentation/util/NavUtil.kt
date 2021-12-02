package com.dynamicdal.dictionary.presentation.util

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.dynamicdal.dictionary.presentation.navigation.Screen
import com.dynamicdal.dictionary.presentation.ui.home.HomeTabs


val CURRENT_TAB = mutableStateOf("")

@Composable
fun currentDestination(navController: NavHostController): NavDestination? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination
}

@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun currentTabRoute(navController: NavHostController): MutableState<String>{
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return mutableStateOf(navBackStackEntry?.destination?.route?: HomeTabs.DEFINITION.route)
}

@Composable
fun currentTab(currentRoute: MutableState<String>, tabs: Array<HomeTabs>): MutableState<String>{
    if(currentRoute.value == Screen.ONBOARDING_ROUTE.route){
        CURRENT_TAB.value = HomeTabs.DEFINITION.id
    }
    else {
        tabs.forEach { tab ->
            if (currentRoute.value == tab.route){
                CURRENT_TAB.value = tab.id
            }
        }
    }
    return CURRENT_TAB

}
