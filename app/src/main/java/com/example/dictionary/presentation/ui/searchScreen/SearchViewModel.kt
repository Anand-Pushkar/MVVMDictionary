package com.example.dictionary.presentation.ui.searchScreen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionary.domain.model.searchSuggestion.SearchSuggestion
import com.example.dictionary.interactors.search_screen.UpdateSearchSuggestion
import com.example.dictionary.network.WordService
import com.example.dictionary.network.searchSuggestions.model.SearchSuggestionDtoMapper
import com.example.dictionary.presentation.ui.searchScreen.SearchScreenEvent.*
import com.example.dictionary.presentation.ui.util.DialogQueue
import com.example.dictionary.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SearchViewModel
@Inject
constructor(
    private val updateSearchSuggestion: UpdateSearchSuggestion,
    val dtoMapper: SearchSuggestionDtoMapper,
    val wordService: WordService,

) : ViewModel() {

    val searchSuggestions: MutableState<List<SearchSuggestion>> = mutableStateOf(listOf())
    val query = mutableStateOf("")
    val dialogQueue = DialogQueue()
    var loading = mutableStateOf(false)


    fun onTriggerEvent(event: SearchScreenEvent){
        viewModelScope.launch {
            try {
                when(event){
                    is OnQueryChangedEvent -> {
                        onQueryChanged(event.query)
                    }

                }

            }catch (e: Exception){
                Log.e(TAG, "onTriggerEvent: Exception: ${e}, ${e.cause}")
            }
        }
    }


    private suspend fun onQueryChanged(query: String) {
        setQuery(query)
        updateSearchSuggestions(query)

    }

    private suspend fun updateSearchSuggestions(query: String){
        resetSearchState()
        if(query != ""){
            loading.value = true
            searchSuggestions.value = getSearchSuggestionsFromNetwork(query)
            if(this.query.value != query){
                resetSearchState()
            }
            loading.value = false
        }

    }

    private suspend fun getSearchSuggestionsFromNetwork(query: String): List<SearchSuggestion>{
        return dtoMapper.toDomainList(
            wordService.getSearchSuggestions(
                searchQuery = query
            )
        )
    }

    private fun resetSearchState(){
        searchSuggestions.value = listOf()
    }

    private fun setQuery(query: String) {
        this.query.value = query
    }
}