package com.example.dictionary.interactors.search_screen

import android.util.Log
import com.example.dictionary.domain.data.DataState
import com.example.dictionary.domain.model.searchSuggestion.SearchSuggestion
import com.example.dictionary.network.WordService
import com.example.dictionary.network.searchSuggestions.model.SearchSuggestionDtoMapper
import com.example.dictionary.util.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.delay

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