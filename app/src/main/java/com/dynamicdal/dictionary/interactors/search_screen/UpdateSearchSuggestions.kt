package com.dynamicdal.dictionary.interactors.search_screen

import android.util.Log
import com.dynamicdal.dictionary.domain.data.DataState
import com.dynamicdal.dictionary.domain.model.searchSuggestion.SearchSuggestion
import com.dynamicdal.dictionary.network.WordService
import com.dynamicdal.dictionary.network.searchSuggestions.model.SearchSuggestionDtoMapper
import com.dynamicdal.dictionary.util.TAG
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UpdateSearchSuggestions(
    val dtoMapper: SearchSuggestionDtoMapper,
    val wordService: WordService
){
    fun execute(
        query: String,
        isNetworkAvailable: Boolean,
    ): Flow<DataState<List<SearchSuggestion>>> = flow{

        try {
            emit(DataState.loading())

            if(isNetworkAvailable){
                var searchSuggestions = getSearchSuggestionsFromNetwork(query)

                if(searchSuggestions.isNotEmpty()){
                    emit(DataState.success(searchSuggestions))
                } else{
                    // if the list is empty then wait for 1 second, the data might be late,
                    // if the list is still empty after 1 second try 1 more time and then emit
                    delay(1000)
                    searchSuggestions = getSearchSuggestionsFromNetwork(query)
                    delay(1000)
                    emit(DataState.success(searchSuggestions))
                }
            }

        }catch (e: Exception){
            Log.e(TAG, "execute: ${e.message}")
            emit(DataState.error<List<SearchSuggestion>>(e.message?: "unknown error"))
        }
    }

    private suspend fun getSearchSuggestionsFromNetwork(query: String): List<SearchSuggestion>{
        return dtoMapper.toDomainList(
            wordService.getSearchSuggestions(
                searchQuery = query
            )
        )
    }
}