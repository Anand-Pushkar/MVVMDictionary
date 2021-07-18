package com.example.dictionary.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.view.WindowCompat
import com.example.dictionary.dataStore.SettingsDataStore
import com.example.dictionary.domain.model.definition.Definition
import com.example.dictionary.domain.model.rhyme.Rhyme
import com.example.dictionary.network.WordService
import com.example.dictionary.network.definition.model.DefinitionDtoMapper
import com.example.dictionary.network.rhyme.model.RhymeDtoMapper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var settingsDataStore: SettingsDataStore

    @Inject
    lateinit var service: WordService

    @Inject
    lateinit var dmapper: DefinitionDtoMapper

    @Inject
    lateinit var rmapper: RhymeDtoMapper

    @Inject
    @Named("meta_data") lateinit var md: String

    @ExperimentalComposeUiApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            DictionaryApp(
                isDarkTheme = settingsDataStore.isDark.value,
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
                searchQuery = "honesty",
                metaData = md
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