package com.dynamicdal.dictionary.presentation

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.plusAssign
import com.dynamicdal.dictionary.presentation.navigation.NavGraph
import com.dynamicdal.dictionary.presentation.theme.TabTheme
import com.dynamicdal.dictionary.presentation.theme.pinkDarkPrimary
import com.dynamicdal.dictionary.presentation.ui.home.HomeTabs
import com.dynamicdal.dictionary.presentation.ui.util.DialogQueue
import com.dynamicdal.dictionary.presentation.util.currentTab
import com.dynamicdal.dictionary.presentation.util.currentTabRoute
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.navigation.animation.AnimatedComposeNavigator


@ExperimentalStdlibApi
@RequiresApi(Build.VERSION_CODES.N)
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun DictionaryApp(
    isDarkTheme: MutableState<Boolean>,
    isNetworkAvailable: MutableState<Boolean>,
    onToggleTheme: () -> Unit,
    finishActivity: () -> Unit,
    setUserName: (String) -> Unit,
    setOnboardingComplete: () -> Unit,
    onboardingComplete: MutableState<Boolean>,
    startDestination: String,
    userName: MutableState<String>,
) {
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
                backgroundColor = if(onboardingComplete.value){ MaterialTheme.colors.primary } else { pinkDarkPrimary },
                scaffoldState = scaffoldState,
                snackbarHost = {
                    scaffoldState.snackbarHostState
                },
            ) {
                AnimatedVisibility(
                    visible = visible,
                    enter = slideInVertically(
                        initialOffsetY = {
                            // Slide in from bottom
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
                        setUserName = setUserName,
                        onboardingComplete = onboardingComplete,
                        startDestination = startDestination,
                        userName = userName
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

