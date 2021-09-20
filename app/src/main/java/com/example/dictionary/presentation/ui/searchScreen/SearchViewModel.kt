package com.example.dictionary.presentation.ui.searchScreen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

    val searchSuggestions: MutableState<List<SearchSuggestion>?> = mutableStateOf(null)
    val query = mutableStateOf("")
    val initialCursorPosition: MutableState<Int> = mutableStateOf(0)
    val cursorPosition = initialCursorPosition.value.takeIf { it <= query.value.length } ?: query.value.length
    val textFieldValue = mutableStateOf(
        TextFieldValue(
            text = query.value,
            selection = TextRange(cursorPosition)
        )
    )

    val dialogQueue = DialogQueue()
    var loading = mutableStateOf(false)


    fun onTriggerEvent(event: SearchScreenEvent){
        viewModelScope.launch {
            try {
                when(event){
                    is OnQueryChangedEvent -> {
                        onQueryChanged(event.query)
                    }

                    is OnTextFieldValueChanged -> {
                        setTextFieldValue(event.tfv)
                    }

                    is OnSearchCleared -> {
                        onSearchCleared()
                    }

                }

            }catch (e: Exception){
                Log.e(TAG, "onTriggerEvent: Exception: ${e}, ${e.cause}")
            }
        }
    }


    private fun onQueryChanged(query: String) {
        setQuery(query)
        updateSearchSuggestions(this.query.value)
    }

    private fun updateSearchSuggestions(query: String){
        resetSearchState()

        if(query.isNotEmpty()){
            updateSearchSuggestion.execute(
                query = query.trim()
            ).onEach { dataState ->

                // if dataState.data is null, this will make loading = true
                if(dataState.data == null){
                    loading.value = dataState.loading
                }


                // data
                dataState.data?.let { ss ->

                    // check if query has changed
                    if(this.query.value != query){
                        // if the value of query has changed
                        if(this.query.value.isEmpty()){
                            loading.value = dataState.loading
                        }
                    }else{
                        // if query has not changed, put values in searchSuggestions
                        if(ss.isNotEmpty()){
                            searchSuggestions.value = ss
                        }
                        loading.value = dataState.loading
                    }
                }

                //error
                dataState.error?.let { error ->
                    dialogQueue.appendErrorMessage("Error", error)
                }

            }.launchIn(viewModelScope)
        }
    }

    private suspend fun getSearchSuggestionsFromNetwork(query: String): List<SearchSuggestion>{
        return dtoMapper.toDomainList(
            wordService.getSearchSuggestions(
                searchQuery = query
            )
        )
    }

    private fun onSearchCleared(){
        query.value = ""
        textFieldValue.value = TextFieldValue(
            text = query.value,
            selection = TextRange(cursorPosition)
        )
        searchSuggestions.value = null
    }

    private fun resetSearchState(){
        searchSuggestions.value = null
    }

    private fun setQuery(query: String) {
        this.query.value = query
    }

    private fun setTextFieldValue(tfv: TextFieldValue){
        this.textFieldValue.value = tfv
    }

}