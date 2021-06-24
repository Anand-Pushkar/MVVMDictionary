package com.example.dictionary.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.example.dictionary.domain.model.definition.Definition
import com.example.dictionary.domain.model.rhyme.Rhyme
import com.example.dictionary.network.WordService
import com.example.dictionary.network.definition.model.DefinitionDtoMapper
import com.example.dictionary.network.rhyme.model.RhymeDtoMapper
import com.example.dictionary.util.TAG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var service: WordService

    @Inject
    lateinit var dmapper: DefinitionDtoMapper

    @Inject
    lateinit var rmapper: RhymeDtoMapper

    @Inject
    @Named("meta_data") lateinit var md: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            lifecycleScope.launch {
                val defs = getDefinitionsFromNetwork()
                Log.d(TAG, "onCreate: ${defs.size}")
                Log.d(TAG, "onCreate: Word: ${defs[0].word}")
                Log.d(TAG, "onCreate: Definitions: ${defs[0].defs?.get(0)}")

                val rhymes = getRhymesFromNetwork()
                Log.d(TAG, "onCreate: ${rhymes.size}")
                Log.d(TAG, "onCreate: Rhyme: ${rhymes[0].word}")
            }
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