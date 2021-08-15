package com.example.dictionary.dataStore

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.createDataStore
import com.example.dictionary.presentation.BaseApplication
import com.example.dictionary.util.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsDataStore
@Inject
constructor(
    app: BaseApplication
){
    private val datastore: DataStore<Preferences> = app.createDataStore(
        name = "settings"
    )

    private val scope = CoroutineScope(Dispatchers.Main)

    init {
        Log.d(TAG, ": dataStore init")
        observeDataStore()
    }

    val onboardingComplete = mutableStateOf(false)
    val isDark = mutableStateOf(true)

    fun toggleTheme(){
        scope.launch {
            datastore.edit { preferences ->
                val current = preferences[DARK_THEME_KEY]?: false
                preferences[DARK_THEME_KEY] = !current
            }
        }
    }

    fun setOnboardingComplete(){
        Log.d(TAG, "setOnboardingComplete: inside the function")
        scope.launch {
            datastore.edit { preferences ->
                preferences[ONBOARDING_KEY] = true
            }
        }
    }

    private fun observeDataStore(){

        datastore.data.onEach { preferences ->

            preferences[DARK_THEME_KEY]?.let { isDarkTheme ->
                isDark.value = isDarkTheme
            }

            preferences[ONBOARDING_KEY]?.let { isOnboardingComplete ->
                onboardingComplete.value = isOnboardingComplete
            }

        }.launchIn(scope)


    }

    companion object{
        private val DARK_THEME_KEY = booleanPreferencesKey("dark_theme_key")
        private val ONBOARDING_KEY = booleanPreferencesKey("onboarding_key")
    }
}