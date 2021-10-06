package com.example.dictionary.interactors.definition_detail_screen

import android.util.Log
import com.example.dictionary.cache.definition.DefinitionDao
import com.example.dictionary.cache.definition.model.DefinitionEntityMapper
import com.example.dictionary.domain.data.DataState
import com.example.dictionary.domain.model.definition.Definition
import com.example.dictionary.network.WordService
import com.example.dictionary.network.definition.model.DefinitionDtoMapper
import com.example.dictionary.util.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RemoveFromFavoriteWords(
    val dtoMapper: DefinitionDtoMapper,
    val wordService: WordService,
    val entityMapper: DefinitionEntityMapper,
    val definitionDao: DefinitionDao
) {
    fun execute(
        definition: Definition,
        isNetworkAvailable: Boolean,
    ): Flow<DataState<Definition>> = flow{

        try {
            // loading
            emit(DataState.loading<Definition>())

            // remove from cache
            definitionDao.deleteWord(entityMapper.mapFromDomainModel(definition))

            // read from the cache
            val def = getWordFromCache(definition.word)

            // if cache still holds the word, delete again, although this should not happen
            if(def != null){
                definitionDao.deleteWord(entityMapper.mapFromDomainModel(definition))
            }

            // get recipe from network and emit
            if(isNetworkAvailable){
                val defs = getDefinitionsFromNetwork(definition.word)
                defs.forEach { def ->
                    if(def.word.compareTo(definition.word.lowercase()) == 0){
                        emit(DataState.success<Definition>(def))
                    }
                }
            }else{
                if (definition != null) {
                    definition.isFavorite = false
                    emit(DataState.success<Definition>(definition))
                }
            }
        }catch (e: Exception){
            Log.e(TAG, "execute: ${e.message}")
            emit(DataState.error<Definition>(e.message?: "unknown error"))
        }
    }

    private suspend fun getDefinitionsFromNetwork(sQuery: String): List<Definition>{
        return dtoMapper.toDomainList(
            wordService.getDefinitions(
                searchQuery = sQuery
            )
        )
    }
    private suspend fun getWordFromCache(word: String): Definition? {
        return definitionDao.getDefinitionByWord(word)?.let { definitionEntity ->
            entityMapper.mapToDomainModel(definitionEntity)
        }
    }
}