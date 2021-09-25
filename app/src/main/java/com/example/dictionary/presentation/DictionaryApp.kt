package com.example.dictionary.presentation

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.plusAssign
import com.example.dictionary.presentation.components.SplashScreen
import com.example.dictionary.presentation.components.bottomNavigationComponent.DictionaryBottomBar
import com.example.dictionary.presentation.navigation.NavGraph
import com.example.dictionary.presentation.theme.TabTheme
import com.example.dictionary.presentation.ui.home.HomeTabs
import com.example.dictionary.presentation.ui.util.DialogQueue
import com.example.dictionary.presentation.util.currentTabRoute
import com.example.dictionary.presentation.util.currentTab
import com.example.dictionary.util.TAG
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.navigation.animation.AnimatedComposeNavigator

//import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.animation.navigation


@ExperimentalStdlibApi
@RequiresApi(Build.VERSION_CODES.N)
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun DictionaryApp(
    showSplashScreen: MutableState<Boolean>,
    isDarkTheme: MutableState<Boolean>,
    isNetworkAvailable: MutableState<Boolean>,
    onToggleTheme: () -> Unit,
    finishActivity: () -> Unit,
    setOnboardingComplete: () -> Unit,
    onboardingComplete: MutableState<Boolean>,
    startDestination: String,
) {
    Log.d(TAG, "DictionaryApp: ===============================================")
    ProvideWindowInsets {
        val tabs = remember { HomeTabs.values() }
        val navController = rememberAnimatedNavController()
        val scaffoldState = rememberScaffoldState()
        val dialogQueue = DialogQueue()

        val currentRoute = currentTabRoute(navController = navController)
        val currentTab = currentTab(currentRoute = currentRoute, tabs = tabs)

        var visible by remember { mutableStateOf(false) }

        TabTheme(
            isDarkTheme = isDarkTheme,
            isNetworkAvailable = mutableStateOf(true), // we don't want to show multiple "no internet" messages
            scaffoldState = scaffoldState,
            dialogQueue = dialogQueue.queue.value, // replace with the reference created in the viewModel
            displayProgressBar = false, // replace with loading
            selectedTab = currentTab
        ) {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize(),
                backgroundColor = MaterialTheme.colors.primary,
                scaffoldState = scaffoldState,
                snackbarHost = {
                    scaffoldState.snackbarHostState
                },
            ) {
                AnimatedVisibility(
                    visible = visible,
                    enter = slideInVertically(
                        initialOffsetY = {
                            // Slide in from top
                            it
                        },
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = CubicBezierEasing(0f, 0f, 0f, 1f)

                        )
                    ),
                ){
                    NavGraph(
                        darkTheme = isDarkTheme,
                        isNetworkAvailable = isNetworkAvailable,
                        finishActivity = finishActivity,
                        onToggleTheme = { onToggleTheme() },
                        navController = navController,
                        setOnboardingComplete = setOnboardingComplete,
                        onboardingComplete = onboardingComplete,
                        startDestination = startDestination
                    )
                }
                LaunchedEffect(true) {
                    visible = true
                }
            }
        }
    }
}

@SuppressLint("RememberReturnType")
@ExperimentalAnimationApi
@Composable
fun rememberAnimatedNavController(): NavHostController {
    val navController = rememberNavController()
    val animatedNavigator = remember(navController) { AnimatedComposeNavigator() }
    return navController.apply {
        navigatorProvider += animatedNavigator
    }
}

