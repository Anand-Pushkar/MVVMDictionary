package com.example.dictionary.presentation.ui.rhymeDetails

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionary.domain.model.rhyme.Rhyme
import com.example.dictionary.domain.model.rhyme.Rhymes
import com.example.dictionary.network.WordService
import com.example.dictionary.network.rhyme.model.RhymeDtoMapper
import com.example.dictionary.presentation.ui.util.DialogQueue
import com.example.dictionary.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RhymeDetailViewModel
@Inject
constructor(
    val dtoMapper: RhymeDtoMapper,
    val wordService: WordService,
) : ViewModel() {

    val rhymes: MutableState<Rhymes?> = mutableStateOf(null)
    val rhymesMap: MutableState<Map<String, List<Rhyme>>?> = mutableStateOf(null)
    val rhymeList: MutableState<List<Rhyme>?> = mutableStateOf(null)
    val dialogQueue = DialogQueue()
    var loading = mutableStateOf(false)
    val onLoad: MutableState<Boolean> = mutableStateOf(false)

    @ExperimentalStdlibApi
    fun onTriggerEvent(event: RhymeDetailScreenEvent){
        viewModelScope.launch {
            try {
                when(event){
                    is RhymeDetailScreenEvent.GetRhymesEvent -> {
                        getRhymes(event.query)
                    }
                }

            }catch (e: Exception){
                Log.e(TAG, "onTriggerEvent: Exception: ${e}, ${e.cause}")
            }
        }
    }

    @ExperimentalStdlibApi
    private suspend fun getRhymes(sQuery: String){
        loading.value = true
        rhymeList.value = getRhymesFromNetwork(sQuery).sortedBy { it.numSyllables } // sort in ascending order of numSyllables
        rhymes.value = Rhymes(
            mainWord = sQuery,
            rhyme = rhymeList.value
        )
        rhymesMap.value = getRhymesMap(rhymes.value)
        loading.value = false
    }

    private suspend fun getRhymesFromNetwork(sQuery: String): List<Rhyme>{
        return dtoMapper.toDomainList(
            wordService.getRhymes(
                searchQuery = sQuery
            )
        )
    }

    @ExperimentalStdlibApi
    private fun getRhymesMap(rhymes: Rhymes?): Map<String, List<Rhyme>>? {
        rhymes?.let {
            it.rhyme?.let { rhymeList ->
                return buildMap<String, List<Rhyme>> {
                    var lowerIndex = 0
                    for(i in 0..(rhymeList.size - 1)){

                        if((i != 0 && rhymeList[i].numSyllables > rhymeList[i - 1].numSyllables) || i == rhymeList.size - 1){

                            if(i == rhymeList.size - 1){
                                this["${rhymeList[i].numSyllables}"] = rhymeList.subList(lowerIndex, i + 1)
                            } else{
                                this["${rhymeList[i - 1].numSyllables}"] = rhymeList.subList(lowerIndex, i)
                                lowerIndex = i
                            }
                        }
                    }
                }
            }
        }
        return null
    }

}