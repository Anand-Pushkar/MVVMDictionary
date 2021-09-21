package com.example.dictionary.presentation.ui.definitionDetails

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionary.domain.model.definition.Definition
import com.example.dictionary.interactors.definition_detail_screen.GetDefinitions
import com.example.dictionary.network.WordService
import com.example.dictionary.network.definition.model.DefinitionDtoMapper
import com.example.dictionary.presentation.ui.util.DialogQueue
import com.example.dictionary.presentation.util.MyConnectivityManager
import com.example.dictionary.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DefinitionDetailViewModel
@Inject
constructor(
    val getDefinitions: GetDefinitions,
    private val myConnectivityManager: MyConnectivityManager,
) : ViewModel() {

    val definition: MutableState<Definition?> = mutableStateOf(null)
    val dialogQueue = DialogQueue()
    var loading = mutableStateOf(false)
    val onLoad: MutableState<Boolean> = mutableStateOf(false)


    fun onTriggerEvent(event: DefinitionDetailScreenEvent){
        viewModelScope.launch {
            try {
                when(event){
                    is DefinitionDetailScreenEvent.GetDefinitionDetailEvent -> {
                        getDefinitions(event.query)
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
            dataState.data?.let { defs ->
                defs.forEach { def ->
                    if(def.word.compareTo(sQuery.lowercase()) == 0){
                        definition.value = def
                    }
                }
            }

            // error
            dataState.error?.let { error ->
                dialogQueue.appendErrorMessage("Error", error)
            }
        }.launchIn(viewModelScope)
    }
}