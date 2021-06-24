package com.example.dictionary.network

import com.example.dictionary.network.definition.model.DefinitionDto
import com.example.dictionary.network.rhyme.model.RhymeDto
import com.example.dictionary.network.searchSuggestions.model.SearchSuggestionDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WordService {

    @GET("words")
    suspend fun getDefinitions(
        @Query("ml") searchQuery: String,
        @Query("md") metaData: String
    ): List<DefinitionDto>

    @GET("words")
    suspend fun getRhymes(
        @Query("rel_rhy") searchQuery: String
    ): List<RhymeDto>

    @GET("sug")
    suspend fun getSearchSuggestions(
        @Query("s") searchQuery: String
    ): List<SearchSuggestionDto>

}