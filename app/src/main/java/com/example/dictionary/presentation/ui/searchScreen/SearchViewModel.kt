package com.example.dictionary.presentation.ui.searchScreen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionary.domain.model.searchSuggestion.SearchSuggestion
import com.example.dictionary.interactors.search_screen.UpdateSearchSuggestions
import com.example.dictionary.presentation.ui.searchScreen.SearchScreenEvent.*
import com.example.dictionary.presentation.ui.util.DialogQueue
import com.example.dictionary.presentation.util.MyConnectivityManager
import com.example.dictionary.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

const val STATE_KEY_QUERY = "search.state.key.query"
const val STATE_KEY_FIRST_VISIBLE_ITEM_INDEX = "search.state.key.first_visible_item_index"
const val STATE_KEY_FIRST_VISIBLE_ITEM_OFFSET = "search.state.key.first_visible_item_offset"

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
    private var firstVisibleItemIndex = 0
    private var firstVisibleItemScrollOffset = 0
    private val restore: MutableState<Boolean> = mutableStateOf(false)

    init {
        /*
         * restoring state after process death
         */
        savedStateHandle.get<Int>(STATE_KEY_FIRST_VISIBLE_ITEM_INDEX)?.let { index ->
            setIndex(index)
        }
        savedStateHandle.get<Int>(STATE_KEY_FIRST_VISIBLE_ITEM_OFFSET)?.let { offset ->
            setOffset(offset)
        }
        savedStateHandle.get<String>(STATE_KEY_QUERY)?.let { query ->
            restore.value = true
            loading.value = true
            onTriggerEvent(OnTextFieldValueChanged(TextFieldValue(
                text = query,
                selection = TextRange(query.length)
            )))
        }
    }


    fun onTriggerEvent(event: SearchScreenEvent) {
        viewModelScope.launch {
            try {
                when (event) {
                    is OnTextFieldValueChanged -> {
                        onTextFieldValueChanged(event.tfv)
                    }

                    is OnSearchCleared -> {
                        onSearchCleared()
                    }

                }

            } catch (e: Exception) {
                Log.e(TAG, "onTriggerEvent: Exception: ${e}, ${e.cause}")
            }
        }
    }

    private fun onTextFieldValueChanged(tfv: TextFieldValue){
        setTextFieldValue(tfv)
        onQueryChanged(tfv.text)
    }

    private fun onQueryChanged(query: String) {
        setQuery(query)
        updateSearchSuggestions(this.query.value)
    }

    private fun updateSearchSuggestions(query: String) {
        Log.d(TAG, "updateSearchSuggestions: here")
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
                        if (ss.isNotEmpty()) {
                            searchSuggestions.value = ss
                            restore.value = false
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


    private fun onSearchCleared() {
        setQuery("")
        setTextFieldValue(
            TextFieldValue(
                text = query.value,
                selection = TextRange(cursorPosition)
            )
        )
        resetSearchSuggestionsState()
    }

    private fun setTextFieldValue(tfv: TextFieldValue) {
        this.textFieldValue.value = tfv
    }

    /**
     * this function reset 2 values :-
     * 1. it empties the searchSuggestions list
     * 2. it sets the scroll position back to 0
     */
    private fun resetSearchSuggestionsState() {
        searchSuggestions.value = null
        if(!restore.value) {
            updateScrollState(0,0)
        }
    }

    private fun setQuery(query: String) {
        this.query.value = query
        savedStateHandle.set(STATE_KEY_QUERY, query)
    }

    fun updateScrollState(index: Int, offset: Int){
        setIndex(index)
        setOffset(offset)
    }

    private fun setIndex(index: Int){
        firstVisibleItemIndex = index
        savedStateHandle.set(STATE_KEY_FIRST_VISIBLE_ITEM_INDEX, index)
    }

    private fun setOffset(offset: Int){
        firstVisibleItemScrollOffset = offset
        savedStateHandle.set(STATE_KEY_FIRST_VISIBLE_ITEM_OFFSET, offset)
    }

    fun getIndex(): Int {
        return firstVisibleItemIndex
    }

    fun getOffset(): Int {
        return firstVisibleItemScrollOffset
    }
}