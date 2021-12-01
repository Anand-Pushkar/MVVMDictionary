package com.example.dictionary.interactors.definition_detail_screen

import android.util.Log
import com.example.dictionary.cache.definition.DefinitionDao
import com.example.dictionary.cache.definition.mapper.DefinitionEntityMapper
import com.example.dictionary.domain.data.DataState
import com.example.dictionary.domain.model.definition.Definition
import com.example.dictionary.util.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AddToFavoriteWords(
    val entityMapper: DefinitionEntityMapper,
    val definitionDao: DefinitionDao
) {
    fun execute(
        definition: Definition,
    ): Flow<DataState<Definition>> = flow {

        try {
            // loading
            emit(DataState.loading<Definition>())

            // insert into cache
            definitionDao.insertWord(entityMapper.mapFromDomainModel(definition))

            // read from the cache
            val def = getWordFromCache(definition.word)

            // if cache holds the word
            if(def != null){
                emit(DataState.success(def))
            } else{
                // should not be null now but if it happens throw exception
                throw Exception("Unable to get the word from the cache.")
            }

        }catch (e: Exception){
            Log.e(TAG, "execute: ${e.message}")
            emit(DataState.error<Definition>(e.message?: "unknown error"))
        }

    }

    private suspend fun getWordFromCache(word: String): Definition? {
        return definitionDao.getDefinitionByWord(word)?.let { definitionEntity ->
            entityMapper.mapToDomainModel(definitionEntity)
        }
    }
}