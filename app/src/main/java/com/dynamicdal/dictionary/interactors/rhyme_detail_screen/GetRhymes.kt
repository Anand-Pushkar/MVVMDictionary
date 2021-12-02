package com.dynamicdal.dictionary.interactors.rhyme_detail_screen

import android.util.Log
import com.dynamicdal.dictionary.cache.rhyme.RhymeDao
import com.dynamicdal.dictionary.cache.rhyme.mapper.RhymeEntityMapper
import com.dynamicdal.dictionary.domain.data.DataState
import com.dynamicdal.dictionary.domain.model.rhyme.Rhyme
import com.dynamicdal.dictionary.domain.model.rhyme.Rhymes
import com.dynamicdal.dictionary.network.WordService
import com.dynamicdal.dictionary.network.rhyme.model.RhymeDtoMapper
import com.dynamicdal.dictionary.util.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@ExperimentalStdlibApi
class GetRhymes(
    val dtoMapper: RhymeDtoMapper,
    val wordService: WordService,
    val entityMapper: RhymeEntityMapper,
    val rhymeDao: RhymeDao
) {
    fun execute(
        query: String,
        isNetworkAvailable: Boolean,
    ): Flow<DataState<Rhymes>> = flow{

        try {

            // loading
            emit(DataState.loading<Rhymes>())

            // first get data from cache (favorites will be retrieved from here)
            val cacheResult = getRhymeFromCache(query)

            // if cache holds the rhymes
            if(cacheResult != null){
                emit(DataState.success(
                    Rhymes(
                        mainWord = query,
                        rhymeList = cacheResult.rhymeList?.sortedBy { it.numSyllables },
                        isFavorite = cacheResult.isFavorite
                    )
                ))
            }
            else {
                if(isNetworkAvailable){
                    val rhymes = getRhymesFromNetwork(query)
                    emit(DataState.success(
                        Rhymes(
                            mainWord = query,
                            rhymeList = rhymes.sortedBy { it.numSyllables }
                        )
                    ))
                }
            }

        }catch (e: Exception){
            Log.e(TAG, "execute: ${e.message}")
            emit(DataState.error<Rhymes>(e.message?: "unknown error"))
        }
    }

    @ExperimentalStdlibApi
    private suspend fun getRhymeFromCache(query: String): Rhymes? {
        return rhymeDao.getWordWithAllTheRhymes(query)?.let {
            entityMapper.mapToRhymesModel(it)
        }
    }

    private suspend fun getRhymesFromNetwork(sQuery: String): List<Rhyme>{
        return dtoMapper.toDomainList(
            wordService.getRhymes(
                searchQuery = sQuery
            )
        )
    }
}