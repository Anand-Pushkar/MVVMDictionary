package com.dynamicdal.dictionary.interactors.my_words_screen

import android.util.Log
import com.dynamicdal.dictionary.cache.definition.DefinitionDao
import com.dynamicdal.dictionary.cache.definition.mapper.DefinitionEntityMapper
import com.dynamicdal.dictionary.domain.data.DataState
import com.dynamicdal.dictionary.domain.model.definition.DefinitionMinimal
import com.dynamicdal.dictionary.util.TAG
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetFavoriteWords(
    val entityMapper: DefinitionEntityMapper,
    val definitionDao: DefinitionDao
) {
    fun execute(): Flow<DataState<List<DefinitionMinimal>>> = flow{

        try {
            // loading
            emit(DataState.loading<List<DefinitionMinimal>>())

            delay(500)

            // get favorite words from cache
            val myWords = getFavoriteWordsFromCache()

            // if cache holds the list
            if(myWords != null){
                emit(DataState.success<List<DefinitionMinimal>>(myWords))
            }
            else{
                throw Exception("Unable to get the words from the cache.")
            }

        }catch (e: Exception){
            Log.e(TAG, "execute: ${e.message}")
            emit(DataState.error<List<DefinitionMinimal>>(e.message?: "unknown error"))
        }
    }

    private suspend fun getFavoriteWordsFromCache(): List<DefinitionMinimal>?{
        return definitionDao.getFavoriteList()?.let { favoriteWordsList ->
            entityMapper.toDomainMinimalList(favoriteWordsList)
        }
    }


}