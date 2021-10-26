package com.example.dictionary.presentation.ui.myRhymes

import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionary.domain.model.rhyme.RhymesMinimal
import com.example.dictionary.interactors.my_rhymes_screen.GetFavoriteRhymes
import com.example.dictionary.presentation.components.util.SnackbarController
import com.example.dictionary.presentation.ui.myWords.MyWordsScreenEvent
import com.example.dictionary.presentation.ui.util.DialogQueue
import com.example.dictionary.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MyRhymesViewModel
@ExperimentalStdlibApi
@Inject
constructor(
    val getFavoriteRhymes: GetFavoriteRhymes
) : ViewModel() {

    val myRhymesList: MutableState<List<RhymesMinimal>?> = mutableStateOf(null)
    val dialogQueue = DialogQueue()
    var loading = mutableStateOf(false)
    val onLoad: MutableState<Boolean> = mutableStateOf(false)

    fun onStart() {
        // start task - the composable has entered the composition
        myRhymesList.value = null // shimmer only shows when this is null
        onLoad.value = false // to get a fresh list from cache
    }

    fun onStop() {
        // cancel task - the composable has left the composition
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
}