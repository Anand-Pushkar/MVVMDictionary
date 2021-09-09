package com.example.dictionary.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.view.WindowCompat
import com.example.dictionary.dataStore.SettingsDataStore
import com.example.dictionary.domain.model.definition.Definition
import com.example.dictionary.domain.model.rhyme.Rhyme
import com.example.dictionary.network.WordService
import com.example.dictionary.network.definition.model.DefinitionDtoMapper
import com.example.dictionary.network.rhyme.model.RhymeDtoMapper
import com.example.dictionary.presentation.util.MyConnectivityManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var myConnectivityManager: MyConnectivityManager

    @Inject
    lateinit var settingsDataStore: SettingsDataStore

    @Inject
    lateinit var service: WordService

    @Inject
    lateinit var dmapper: DefinitionDtoMapper

    @Inject
    lateinit var rmapper: RhymeDtoMapper


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
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            DictionaryApp(
                isDarkTheme = settingsDataStore.isDark,
                isNetworkAvailable = myConnectivityManager.isNetworkAvailable,
                onToggleTheme = { settingsDataStore.toggleTheme() },
                finishActivity = { finish() },
                setOnboardingComplete = { settingsDataStore::setOnboardingComplete },
                onboardingComplete = settingsDataStore.onboardingComplete
            )
        }
    }

    private suspend fun getDefinitionsFromNetwork(): List<Definition>{
        return dmapper.toDomainList(
            service.getDefinitions(
                searchQuery = "honesty"
            )
        )
    }

    private suspend fun getRhymesFromNetwork(): List<Rhyme>{
        return rmapper.toDomainList(
            service.getRhymes(
                searchQuery = "honest"
            )
        )
    }
}