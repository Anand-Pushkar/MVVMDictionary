package com.dynamicdal.dictionary.presentation.ui.searchScreen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dynamicdal.dictionary.domain.model.searchSuggestion.SearchSuggestion
import com.dynamicdal.dictionary.interactors.search_screen.UpdateSearchSuggestions
import com.dynamicdal.dictionary.presentation.ui.util.DialogQueue
import com.dynamicdal.dictionary.presentation.util.MyConnectivityManager
import com.dynamicdal.dictionary.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

const val STATE_KEY_QUERY = "search.state.key.query"

@HiltViewModel
class SearchViewModel
@Inject
constructor(
    private val updateSearchSuggestion: UpdateSearchSuggestions,
    private val myConnectivityManager: MyConnectivityManager,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val searchSuggestions: MutableState<List<SearchSuggestion>?> = mutableStateOf(null)
    val query = mutableStateOf("")
    private val initialCursorPosition: MutableState<Int> = mutableStateOf(0)
    private val cursorPosition = initialCursorPosition.value.takeIf { it <= query.value.length } ?: query.value.length
    val textFieldValue = mutableStateOf(
        TextFieldValue(
            text = query.value,
            selection = TextRange(cursorPosition)
        )
    )
    val dialogQueue = DialogQueue()
    var loading = mutableStateOf(false)
    val comingBack = mutableStateOf(false)

    init {
        /*
         * restoring state after process death
         */
        savedStateHandle.get<String>(STATE_KEY_QUERY)?.let { query ->
            loading.value = true
            onTriggerEvent(
                SearchScreenEvent.OnTextFieldValueChanged(
                    TextFieldValue(
                        text = query,
                        selection = TextRange(query.length)
                    )
                )
            )
        }
    }


    fun onTriggerEvent(event: SearchScreenEvent) {
        viewModelScope.launch {
            try {
                when (event) {
                    is SearchScreenEvent.OnTextFieldValueChanged -> {
                        updateSearchSuggestions(event.tfv)
                    }
                    is SearchScreenEvent.OnSearchCleared -> {
                        onSearchCleared()
                    }

                }

            } catch (e: Exception) {
                Log.e(TAG, "onTriggerEvent: Exception: ${e}, ${e.cause}")
            }
        }
    }


    // use case 1
    private fun updateSearchSuggestions(tfv: TextFieldValue) {
        setTextFieldValue(tfv)
        setQuery(tfv.text)
        val query = tfv.text
        resetSearchSuggestionsState()

        if (query.isNotEmpty()) {
            updateSearchSuggestion.execute(
                query = query.trim(),
                isNetworkAvailable = myConnectivityManager.isNetworkAvailable.value
            ).onEach { dataState ->

                // if dataState.data is null, this will make loading = true
                if (dataState.data == null) {
                    loading.value = dataState.loading
                }

                // data
                dataState.data?.let { ss ->

                    // check if query has changed
                    if (this.query.value != query) {
                        // if the value of query has changed
                        if (this.query.value.isEmpty()) {
                            loading.value = dataState.loading
                        }
                    } else {
                        // if query has not changed, put values in searchSuggestions
                        if (ss.isNotEmpty()) { // if ss is empty, we do not put any value in searchSuggestions, leaving it null
                            searchSuggestions.value = ss
                        }
                        loading.value = dataState.loading
                    }
                }

                // error
                dataState.error?.let { error ->
                    dialogQueue.appendErrorMessage("Error", error)
                }

            }.launchIn(viewModelScope)
        }
    }


    // use case 2
    private fun onSearchCleared() {
        setQuery("")
        setTextFieldValue(
            TextFieldValue(
                text = query.value,
                selection = TextRange(cursorPosition)
            )
        )
        // we want to reset index and offset when search is cleared
        resetSearchSuggestionsState()
    }


    private fun resetSearchSuggestionsState() {
        searchSuggestions.value = null
    }

    private fun setTextFieldValue(tfv: TextFieldValue) {
        this.textFieldValue.value = tfv
    }

    private fun setQuery(query: String) {
        this.query.value = query
        savedStateHandle.set(STATE_KEY_QUERY, query)
    }

}