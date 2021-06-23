package com.example.dictionary.network

import com.example.dictionary.domain.model.searchSuggestion.SearchSuggestion
import com.example.dictionary.network.definition.response.DefinitionsResponse
import com.example.dictionary.network.rhyme.response.RhymesResponse
import com.example.dictionary.network.searchSuggestions.response.SearchSuggestionsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WordService {

    @GET("words")
    suspend fun getDefinitions(
        @Query("ml") searchQuery: String,
        @Query("md") metaData: String
    ): DefinitionsResponse

    @GET("words")
    suspend fun getRhymes(
        @Query("rel_rhy") searchQuery: String
    ): RhymesResponse

    @GET("sug")
    suspend fun getSearchSuggestions(
        @Query("s") searchQuery: String
    ): SearchSuggestionsResponse

}