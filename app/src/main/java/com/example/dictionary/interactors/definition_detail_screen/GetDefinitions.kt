package com.example.dictionary.interactors.definition_detail_screen

import android.util.Log
import com.example.dictionary.cache.definition.DefinitionDao
import com.example.dictionary.cache.definition.mapper.DefinitionEntityMapper
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
    val entityMapper: DefinitionEntityMapper,
    val definitionDao: DefinitionDao
){
    fun execute(
        query: String,
        isNetworkAvailable: Boolean,
    ): Flow<DataState<Definition>> = flow{

        try {
            // loading
            emit(DataState.loading<Definition>())

            // get definition from the cache (favorites will be retrieved from here)
            val def = getWordFromCache(query)

            if(def != null){ // if cache holds the definition
                emit(DataState.success<Definition>(def))
            }
            else{ // get definition from network
                if(isNetworkAvailable){
                    val defs = getDefinitionsFromNetwork(query)
                    defs.forEach { def ->
                        if(def.word.compareTo(query.lowercase()) == 0){
                            emit(DataState.success<Definition>(def))
                        }else{
                            emit(DataState.error<Definition>("Invalid Search"))
                        }
                    }
                }
            }
        }catch (e: Exception){
            Log.e(TAG, "execute: ${e.message}")
            emit(DataState.error<Definition>(e.message?: "unknown error"))
        }
    }

    private suspend fun getDefinitionsFromNetwork(sQuery: String): List<Definition>{
        return dtoMapper.toDomainList(
            wordService.getDefinitions(
                searchQuery = sQuery
            )
        )
    }
    private suspend fun getWordFromCache(word: String): Definition? {
        return definitionDao.getDefinitionByWord(word)?.let { definitionEntity ->
            entityMapper.mapToDomainModel(definitionEntity)
        }
    }
}