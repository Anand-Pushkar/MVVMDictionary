package com.example.dictionary.presentation.ui.definitionDetails

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionary.domain.model.definition.Definition
import com.example.dictionary.network.WordService
import com.example.dictionary.network.definition.model.DefinitionDtoMapper
import com.example.dictionary.presentation.ui.searchScreen.SearchScreenEvent
import com.example.dictionary.presentation.ui.util.DialogQueue
import com.example.dictionary.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DefinitionDetailViewModel
@Inject
constructor(
    val dtoMapper: DefinitionDtoMapper,
    val wordService: WordService,
) : ViewModel() {

    val definitons: MutableState<List<Definition>?> = mutableStateOf(null)
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

    private suspend fun getDefinitions(sQuery: String){
        loading.value = true
        definitons.value = getDefinitionsFromNetwork(sQuery)
        definitons.value?.forEach { def ->
            if(def.word.compareTo(sQuery.lowercase()) == 0){
                 definition.value = def
            }
        }
        loading.value = false
    }

    private suspend fun getDefinitionsFromNetwork(sQuery: String): List<Definition>{
        return dtoMapper.toDomainList(
            wordService.getDefinitions(
                searchQuery = sQuery
            )
        )
    }
}