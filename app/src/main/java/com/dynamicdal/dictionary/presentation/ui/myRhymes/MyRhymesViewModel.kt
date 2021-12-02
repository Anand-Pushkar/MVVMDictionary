package com.dynamicdal.dictionary.presentation.ui.myRhymes

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dynamicdal.dictionary.domain.model.rhyme.RhymesMinimal
import com.dynamicdal.dictionary.interactors.my_rhymes_screen.GetFavoriteRhymes
import com.dynamicdal.dictionary.presentation.ui.util.DialogQueue
import com.dynamicdal.dictionary.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

const val STATE_KEY_LIST_POSITION = "my_rhymes.state.list_position"

@ExperimentalStdlibApi
@HiltViewModel
class MyRhymesViewModel
@ExperimentalStdlibApi
@Inject
constructor(
    private val getFavoriteRhymes: GetFavoriteRhymes,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val myRhymesList: MutableState<List<RhymesMinimal>?> = mutableStateOf(null)
    val dialogQueue = DialogQueue()
    var loading = mutableStateOf(false)
    var comingBack: MutableState<Boolean> = mutableStateOf(false)
    private var listScrollPosition = 0

    init {
        savedStateHandle.get<Int>(STATE_KEY_LIST_POSITION)?.let { position ->
            setScrollPosition(position)
        }

        // fire a one-off event to get the rhymes from cache
        onTriggerEvent(MyRhymesScreenEvent.GetFavoriteRhymesEvent)
    }

    // start task - the composable has entered the composition
    fun onStart() {
        // to get the fresh list when coming back to this screen
        if(myRhymesList.value == null && !loading.value){
            onTriggerEvent(MyRhymesScreenEvent.GetFavoriteRhymesEvent)
        }
    }

    // cancel task - the composable has left the composition
    fun onStop() {
        comingBack.value = true

        // clearing the list so that we can fetch a fresh list on onStart
        myRhymesList.value = null
    }

    @ExperimentalStdlibApi
    fun onTriggerEvent(event: MyRhymesScreenEvent){
        viewModelScope.launch {
            try {
                when(event){
                    is MyRhymesScreenEvent.GetFavoriteRhymesEvent -> {
                        getFavoriteRhymes()
                    }
                }

            }catch (e: Exception){
                Log.e(TAG, "onTriggerEvent: Exception: ${e}, ${e.cause}")
            }
        }
    }

    @ExperimentalStdlibApi
    private fun getFavoriteRhymes() {
        getFavoriteRhymes.execute().onEach { dataState ->

            // loading
            loading.value = dataState.loading

            // data
            dataState.data?.let { myRhymes ->
                myRhymesList.value = myRhymes
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