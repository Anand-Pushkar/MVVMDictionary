package com.example.dictionary.presentation.ui.definitionDetails

import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionary.domain.model.definition.Definition
import com.example.dictionary.interactors.definition_detail_screen.AddToFavoriteWords
import com.example.dictionary.interactors.definition_detail_screen.GetDefinitions
import com.example.dictionary.interactors.definition_detail_screen.RemoveFromFavoriteWords
import com.example.dictionary.presentation.components.util.SnackbarController
import com.example.dictionary.presentation.ui.util.DialogQueue
import com.example.dictionary.presentation.util.MyConnectivityManager
import com.example.dictionary.util.TAG
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
    val getDefinitions: GetDefinitions,
    val addToFavoriteWords: AddToFavoriteWords,
    val removeFromFavoriteWords: RemoveFromFavoriteWords,
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
                dialogQueue.appendErrorMessage("Error", error)
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
                        message = "${word} added to Favorites!",
                        actionLabel = "THANKS"
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
                        message = "${word} removed from Favorites!",
                        actionLabel = "THANKS"
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