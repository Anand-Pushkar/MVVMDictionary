package com.example.dictionary.presentation.ui.myWords

import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionary.domain.model.definition.DefinitionMinimal
import com.example.dictionary.interactors.my_words_screen.GetFavoriteWords
import com.example.dictionary.presentation.components.util.SnackbarController
import com.example.dictionary.presentation.ui.definitionDetails.DefinitionDetailScreenEvent
import com.example.dictionary.presentation.ui.util.DialogQueue
import com.example.dictionary.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

const val STATE_KEY_LIST_POSITION = "my_words.state.list_position"

@ExperimentalMaterialApi
@HiltViewModel
class MyWordsViewModel
@Inject
constructor(
    private val getFavoriteWords: GetFavoriteWords,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val myWordsList: MutableState<List<DefinitionMinimal>?> = mutableStateOf(null)
    val dialogQueue = DialogQueue()
    var loading = mutableStateOf(false)
    var comingBack: MutableState<Boolean> = mutableStateOf(false)
    private var listScrollPosition = 0

    init {
        savedStateHandle.get<Int>(STATE_KEY_LIST_POSITION)?.let { position ->
            setScrollPosition(position)
        }

        // fire a one off event to get the words from the cache
        onTriggerEvent(MyWordsScreenEvent.GetFavoriteWordsEvent)
    }

    // start task - the composable has entered the composition
    fun onStart() {
        // to get the fresh list when coming back to this screen
        if(myWordsList.value == null && !loading.value){
            onTriggerEvent(MyWordsScreenEvent.GetFavoriteWordsEvent)
        }
    }

    // cancel task - the composable has left the composition
    fun onStop() {
        comingBack.value = true

        // clearing the list so that we can fetch a fresh list on onStart
        myWordsList.value = null
    }


    @ExperimentalMaterialApi
    fun onTriggerEvent(event: MyWordsScreenEvent){
        viewModelScope.launch {
            try {
                when(event){
                    is MyWordsScreenEvent.GetFavoriteWordsEvent -> {
                        getFavoriteWords()
                    }
                }

            }catch (e: Exception){
                Log.e(TAG, "onTriggerEvent: Exception: ${e}, ${e.cause}")
            }
        }
    }

    private fun getFavoriteWords(){
        
        getFavoriteWords.execute().onEach { dataState ->

            // loading
            loading.value = dataState.loading


            // data
            dataState.data?.let { myWords ->
                myWordsList.value = myWords
            }

            // error
            dataState.error?.let { error ->
                dialogQueue.appendErrorMessage("Error", error)
            }
        }.launchIn(viewModelScope)
    }

    fun onChangeScrollPosition(position: Int) {
        setScrollPosition(position)
    }

    fun getListScrollPosition(): Int {
        return listScrollPosition
    }

    fun resetScrollPosition() {
        listScrollPosition = 0
    }

    /**
     * saving state for restoration after process death
     * these functions act as setters for the variables and the constants for state
     */
    private fun setScrollPosition(position: Int) {
        listScrollPosition = position
        savedStateHandle.set(STATE_KEY_LIST_POSITION, position)
    }

}