package com.example.dictionary.interactors.rhyme_detail_screen

import android.util.Log
import com.example.dictionary.domain.data.DataState
import com.example.dictionary.domain.model.rhyme.Rhyme
import com.example.dictionary.network.WordService
import com.example.dictionary.network.rhyme.model.RhymeDtoMapper
import com.example.dictionary.util.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetRhymes(
    val dtoMapper: RhymeDtoMapper,
    val wordService: WordService,
) {
    fun execute(
        query: String,
        isNetworkAvailable: Boolean,
    ): Flow<DataState<List<Rhyme>>> = flow{

        try {

            // loading
            emit(DataState.loading<List<Rhyme>>())

            // data
            if(isNetworkAvailable){
                val rhymes = getRhymesFromNetwork(query)
                emit(DataState.success(rhymes))
            }

        }catch (e: Exception){
            Log.e(TAG, "execute: ${e.message}")
            emit(DataState.error<List<Rhyme>>(e.message?: "unknown error"))
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