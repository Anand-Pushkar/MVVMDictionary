package com.example.dictionary.presentation

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.example.dictionary.R
import com.example.dictionary.dataStore.SettingsDataStore
import com.example.dictionary.presentation.navigation.Screen
import com.example.dictionary.presentation.util.MyConnectivityManager
import com.example.dictionary.util.TAG
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
                                setOnboardingComplete = { settingsDataStore.setOnboardingComplete() },
                                onboardingComplete = onboardingComplete,
                                startDestination = startDestination
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
                    setOnboardingComplete = { settingsDataStore.setOnboardingComplete() },
                    onboardingComplete = onboardingComplete,
                    startDestination = startDestination
                )
            }
        }

        splashScreen.setKeepVisibleCondition{ settingsDataStore.showSplashScreen.value }
    }

    private fun getStartDestination(onboardingComplete: Boolean): String {

        return if(onboardingComplete){
            Log.d(TAG, "getStartDestination: ${Screen.HOME_ROUTE.route}")
            Screen.HOME_ROUTE.route
        }else{
            Log.d(TAG, "getStartDestination: ${Screen.ONBOARDING_ROUTE.route}")
            Screen.ONBOARDING_ROUTE.route
        }
    }

}