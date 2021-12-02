package com.dynamicdal.dictionary.presentation.ui.definitionDetails

import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dynamicdal.dictionary.domain.model.definition.Definition
import com.dynamicdal.dictionary.interactors.definition_detail_screen.AddToFavoriteWords
import com.dynamicdal.dictionary.interactors.definition_detail_screen.GetDefinitions
import com.dynamicdal.dictionary.interactors.definition_detail_screen.RemoveFromFavoriteWords
import com.dynamicdal.dictionary.presentation.components.util.SnackbarController
import com.dynamicdal.dictionary.presentation.ui.util.DialogQueue
import com.dynamicdal.dictionary.presentation.util.MyConnectivityManager
import com.dynamicdal.dictionary.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DefinitionDetailViewModel
@Inject
constructor(
    private val getDefinitions: GetDefinitions,
    private val addToFavoriteWords: AddToFavoriteWords,
    private val removeFromFavoriteWords: RemoveFromFavoriteWords,
    private val myConnectivityManager: MyConnectivityManager,
) : ViewModel() {

    val definition: MutableState<Definition?> = mutableStateOf(null)
    val dialogQueue = DialogQueue()
    var loading = mutableStateOf(false)
    val onLoad: MutableState<Boolean> = mutableStateOf(false)
    val snackbarController = SnackbarController(viewModelScope)


    @ExperimentalMaterialApi
    fun onTriggerEvent(event: DefinitionDetailScreenEvent){
        viewModelScope.launch {
            try {
                when(event){
                    is DefinitionDetailScreenEvent.GetDefinitionDetailEvent -> {
                        getDefinitions(event.query)
                    }
                    is DefinitionDetailScreenEvent.AddToFavoritesEvent -> {
                        addToFavorites(event.scaffoldState)
                    }
                    is DefinitionDetailScreenEvent.RemoveFromFavoritesEvent -> {
                        removeFromFavorites(event.scaffoldState)
                    }
                }

            }catch (e: Exception){
                Log.e(TAG, "onTriggerEvent: Exception: ${e}, ${e.cause}")
            }
        }
    }

    private fun getDefinitions(sQuery: String){

        getDefinitions.execute(
            query = sQuery,
            isNetworkAvailable = myConnectivityManager.isNetworkAvailable.value
        ).onEach { dataState ->

            // loading
            loading.value = dataState.loading

            // data
            dataState.data?.let { def ->
                definition.value = def
            }

            // error
            dataState.error?.let { error ->
                if(error == "Invalid Search"){
                    // do nothing or maybe show snackbar
                }else{
                    dialogQueue.appendErrorMessage("Error", error)
                }
            }

        }.launchIn(viewModelScope)
    }

    @ExperimentalMaterialApi
    private fun addToFavorites(scaffoldState: ScaffoldState) {
        definition.value?.let { definition ->
            addToFavoriteWords.execute(
                definition = definition
            ).onEach { dataState ->

                // loading
                loading.value = dataState.loading

                // data
                dataState.data?.let { def ->

                    // populating the data class object with the values from the cache after successful insertion into the cache
                    this.definition.value = def

                    // show snackbar
                    val word = def.word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                    snackbarController.showSnackbar(
                        scaffoldState = scaffoldState,
                        message = "${word} added to Favourites!",
                        actionLabel = "OK"
                    )
                }

                // error
                dataState.error?.let { error ->
                    dialogQueue.appendErrorMessage("Error", error)
                }
            }.launchIn(viewModelScope)
        }
    }

    @ExperimentalMaterialApi
    private fun removeFromFavorites(scaffoldState: ScaffoldState) {
        definition.value?.let { definition ->
            removeFromFavoriteWords.execute(
                definition = definition,
                isNetworkAvailable = myConnectivityManager.isNetworkAvailable.value
            ).onEach { dataState ->

                // loading
                loading.value = dataState.loading

                // data
                dataState.data?.let { def ->

                    // populating the data class object with the values from the network after successful deletion from the cache
                    this.definition.value = def

                    // show snackbar
                    val word = def.word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                    snackbarController.showSnackbar(
                        scaffoldState = scaffoldState,
                        message = "${word} removed from Favourites!",
                        actionLabel = "OK"
                    )
                }

                // error
                dataState.error?.let { error ->
                    dialogQueue.appendErrorMessage("Error", error)
                }
            }.launchIn(viewModelScope)
        }
    }


}