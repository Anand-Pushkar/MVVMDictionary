package com.example.dictionary.interactors.definition_detail_screen

import android.util.Log
import com.example.dictionary.domain.data.DataState
import com.example.dictionary.domain.model.definition.Definition
import com.example.dictionary.network.WordService
import com.example.dictionary.network.definition.model.DefinitionDtoMapper
import com.example.dictionary.util.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetDefinitions(
    val dtoMapper: DefinitionDtoMapper,
    val wordService: WordService,
){
    fun execute(
        query: String,
        isNetworkAvailable: Boolean,
    ): Flow<DataState<List<Definition>>> = flow{

        try {
            // loading
            emit(DataState.loading())

            // will make it cache first/ offline first after adding cache layer

            // data
            if(isNetworkAvailable){
                val defs = getDefinitionsFromNetwork(query)
                emit(DataState.success(defs))
            }

        }catch (e: Exception){
            Log.e(TAG, "execute: ${e.message}")
            emit(DataState.error<List<Definition>>(e.message?: "unknown error"))
        }

    }

    private suspend fun getDefinitionsFromNetwork(sQuery: String): List<Definition>{
        return dtoMapper.toDomainList(
            wordService.getDefinitions(
                searchQuery = sQuery
            )
        )
    }
}