package com.dynamicdal.dictionary.interactors.my_rhymes_screen

import android.util.Log
import com.dynamicdal.dictionary.cache.rhyme.RhymeDao
import com.dynamicdal.dictionary.cache.rhyme.mapper.RhymeEntityMapper
import com.dynamicdal.dictionary.domain.data.DataState
import com.dynamicdal.dictionary.domain.model.rhyme.RhymesMinimal
import com.dynamicdal.dictionary.util.TAG
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@ExperimentalStdlibApi
class GetFavoriteRhymes(
    val entityMapper: RhymeEntityMapper,
    val rhymeDao: RhymeDao
) {
    fun execute(): Flow<DataState<List<RhymesMinimal>>> = flow {

        try {
            // loading
            emit(DataState.loading<List<RhymesMinimal>>())

            delay(500)

            // get favorite rhymes from the cache
            val myRhymes = getFavoriteRhymesFromCache()

            // if cache holds the list
            if(myRhymes != null){
                emit(DataState.success(myRhymes))
            }
            else{
                throw Exception("Unable to get the rhymes from the cache.")
            }

        }catch (e: Exception){
            Log.e(TAG, "execute: ${e.message}")
            emit(DataState.error<List<RhymesMinimal>>(e.message?: "unknown error"))
        }
    }

    private suspend fun getFavoriteRhymesFromCache(): List<RhymesMinimal>? {
        return rhymeDao.getMyRhymes()?.let { favoriteRhymesList ->
            entityMapper.toDomainMinimalList(favoriteRhymesList)
        }
    }
}