package com.example.dictionary.presentation.ui.myWords

import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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


@HiltViewModel
class MyWordsViewModel
@Inject
constructor(
    val getFavoriteWords: GetFavoriteWords
) : ViewModel() {

    val myWordsList: MutableState<List<DefinitionMinimal>?> = mutableStateOf(null)
    val dialogQueue = DialogQueue()
    var loading = mutableStateOf(false)
    val onLoad: MutableState<Boolean> = mutableStateOf(false)

    fun onStart() {
        // start task - the composable has entered the composition
        myWordsList.value = null // shimmer only shows when this is null
        onLoad.value = false // to get a fresh list from cache
    }

    fun onStop() {
        // cancel task - the composable has left the composition
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
}