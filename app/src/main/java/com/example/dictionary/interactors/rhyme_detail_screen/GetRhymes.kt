package com.example.dictionary.interactors.rhyme_detail_screen

import android.util.Log
import com.example.dictionary.cache.rhyme.RhymeDao
import com.example.dictionary.cache.rhyme.mapper.RhymeEntityMapper
import com.example.dictionary.domain.data.DataState
import com.example.dictionary.domain.model.rhyme.Rhyme
import com.example.dictionary.domain.model.rhyme.Rhymes
import com.example.dictionary.network.WordService
import com.example.dictionary.network.rhyme.model.RhymeDtoMapper
import com.example.dictionary.util.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetRhymes @ExperimentalStdlibApi constructor(
    val dtoMapper: RhymeDtoMapper,
    val wordService: WordService,
    val entityMapper: RhymeEntityMapper,
    val rhymeDao: RhymeDao
) {
    @ExperimentalStdlibApi
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
                Log.d(TAG, "execute: emitting cache result")
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
                    Log.d(TAG, "execute: emitting network result")
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