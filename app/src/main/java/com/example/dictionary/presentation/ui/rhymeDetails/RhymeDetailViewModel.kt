package com.example.dictionary.presentation.ui.rhymeDetails

import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionary.domain.model.rhyme.Rhyme
import com.example.dictionary.domain.model.rhyme.Rhymes
import com.example.dictionary.interactors.rhyme_detail_screen.AddToFavoriteRhymes
import com.example.dictionary.interactors.rhyme_detail_screen.GetRhymes
import com.example.dictionary.interactors.rhyme_detail_screen.RemoveFromFavoriteRhymes
import com.example.dictionary.network.WordService
import com.example.dictionary.network.rhyme.model.RhymeDtoMapper
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
class RhymeDetailViewModel
@ExperimentalStdlibApi
@Inject
constructor(
    val getRhymes: GetRhymes,
    val addToFavoriteRhymes: AddToFavoriteRhymes,
    val removeFromFavoriteRhymes: RemoveFromFavoriteRhymes,
    private val myConnectivityManager: MyConnectivityManager,
) : ViewModel() {

    @ExperimentalStdlibApi
    val rhymes: MutableState<Rhymes?> = mutableStateOf(null)
    val dialogQueue = DialogQueue()
    var loading = mutableStateOf(false)
    val onLoad: MutableState<Boolean> = mutableStateOf(false)
    val snackbarController = SnackbarController(viewModelScope)

    @ExperimentalMaterialApi
    @ExperimentalStdlibApi
    fun onTriggerEvent(event: RhymeDetailScreenEvent){
        viewModelScope.launch {
            try {
                when(event){
                    is RhymeDetailScreenEvent.GetRhymesEvent -> {
                        getRhymes(event.query)
                    }
                    is RhymeDetailScreenEvent.AddToFavoritesEvent -> {
                        addToFavorites(event.scaffoldState)
                    }
                    is RhymeDetailScreenEvent.RemoveFromFavoritesEvent -> {
                        removeFromFavorites(event.scaffoldState)
                    }
                }

            }catch (e: Exception){
                Log.e(TAG, "onTriggerEvent: Exception: ${e}, ${e.cause}")
            }
        }
    }


    @ExperimentalStdlibApi
    private fun getRhymes(sQuery: String){

        getRhymes.execute(
            query = sQuery,
            isNetworkAvailable = myConnectivityManager.isNetworkAvailable.value
        ).onEach { dataState ->

            // loading
            loading.value = dataState.loading

            // data
            dataState.data?.let {
                rhymes.value = it
            }

            // error
            dataState.error?.let { error ->
                dialogQueue.appendErrorMessage("Error", error)
            }

        }.launchIn(viewModelScope)
    }

    @ExperimentalMaterialApi
    @ExperimentalStdlibApi
    private fun addToFavorites(scaffoldState: ScaffoldState) {
        rhymes.value?.let { rhymes ->
            addToFavoriteRhymes.execute(
                rhymes = rhymes
            ).onEach { dataState ->

                // loading
                loading.value = dataState.loading

                // data
                dataState.data?.let {

                    // populating the data class object with the values from the cache after successful insertion into the cache
                    this.rhymes.value = it

                    // show snackbar
                    val rhyme = it.mainWord.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                    snackbarController.showSnackbar(
                        scaffoldState = scaffoldState,
                        message = "${rhyme} added to Favourites!",
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
    @ExperimentalStdlibApi
    private fun removeFromFavorites(scaffoldState: ScaffoldState) {
        rhymes.value?.let { rhymes ->
            removeFromFavoriteRhymes.execute(
                rhymes = rhymes,
                isNetworkAvailable = myConnectivityManager.isNetworkAvailable.value
            ).onEach { dataState ->

                // loading
                loading.value = dataState.loading

                // data
                dataState.data?.let {

                    // populating the data class object with the values from the network after successful deletion from the cache
                    this.rhymes.value = it

                    // show snackbar
                    val rhyme = it.mainWord.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                    snackbarController.showSnackbar(
                        scaffoldState = scaffoldState,
                        message = "${rhyme} removed from Favourites!",
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