package com.dynamicdal.dictionary.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.dynamicdal.dictionary.R
import com.dynamicdal.dictionary.dataStore.SettingsDataStore
import com.dynamicdal.dictionary.presentation.navigation.Screen
import com.dynamicdal.dictionary.presentation.util.MyConnectivityManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var myConnectivityManager: MyConnectivityManager

    @Inject
    lateinit var settingsDataStore: SettingsDataStore
    
    override fun onStart() {
        super.onStart()
        myConnectivityManager.registerConnectionObserver(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        myConnectivityManager.unregisterConnectionObserver(this)
    }

    @ExperimentalStdlibApi
    @RequiresApi(Build.VERSION_CODES.N)
    @ExperimentalAnimationApi
    @ExperimentalMaterialApi
    @ExperimentalComposeUiApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashScreen = installSplashScreen()

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val isDark = settingsDataStore.getIsDarkValue()
        val onboardingComplete = settingsDataStore.getOnboardingCompleteValue()
        val userName = settingsDataStore.getUserName()

        if(settingsDataStore.showSplashScreen.value){

            splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
                // Get icon instance and start a fade out animation
                splashScreenViewProvider.iconView
                    .animate()
                    .alpha(0f)
                    .withEndAction {
                        // After the fade out, remove the splash and set content view
                        splashScreenViewProvider.remove()
                        setContent{

                            val startDestination = getStartDestination(onboardingComplete.value)

                            DictionaryApp(
                                isDarkTheme = isDark,
                                isNetworkAvailable = myConnectivityManager.isNetworkAvailable,
                                onToggleTheme = { settingsDataStore.toggleTheme() },
                                finishActivity = { finish() },
                                setUserName = { settingsDataStore.setUserName(it) },
                                setOnboardingComplete = { settingsDataStore.setOnboardingComplete() },
                                onboardingComplete = onboardingComplete,
                                startDestination = startDestination,
                                userName = userName
                            )
                        }
                    }.start()
            }
        }else{
            setTheme(R.style.Theme_Dictionary_NoActionBar)
            setContent {
                val startDestination = getStartDestination(onboardingComplete.value)

                DictionaryApp(
                    isDarkTheme = isDark,
                    isNetworkAvailable = myConnectivityManager.isNetworkAvailable,
                    onToggleTheme = { settingsDataStore.toggleTheme() },
                    finishActivity = { finish() },
                    setUserName = { settingsDataStore.setUserName(it) },
                    setOnboardingComplete = { settingsDataStore.setOnboardingComplete() },
                    onboardingComplete = onboardingComplete,
                    startDestination = startDestination,
                    userName = userName
                )
            }
        }

        splashScreen.setKeepVisibleCondition{ settingsDataStore.showSplashScreen.value }
    }

    private fun getStartDestination(onboardingComplete: Boolean): String {

        return if(onboardingComplete){
            Screen.HOME_ROUTE.route
        }else{
            Screen.ONBOARDING_ROUTE.route
        }
    }

}