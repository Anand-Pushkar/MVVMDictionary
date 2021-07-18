package com.example.dictionary.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.dictionary.presentation.navigation.Screen
import com.example.dictionary.presentation.ui.home.HomeTabs

@Composable
fun currentRoute(navController: NavController): String {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route?: HomeTabs.DEFINITION.route
}

@Composable
fun currentTab(currentRoute: String, tabs: Array<HomeTabs>): String{
    if(currentRoute != Screen.ONBOARDING_ROUTE.route){
        tabs.forEach { tab ->
            if (currentRoute == tab.route){
                return tab.id
            }
        }
    }
    return HomeTabs.DEFINITION.id
}