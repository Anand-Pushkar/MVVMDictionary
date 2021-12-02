package com.dynamicdal.dictionary.network

import com.dynamicdal.dictionary.network.definition.model.DefinitionDto
import com.dynamicdal.dictionary.network.rhyme.model.RhymeDto
import com.dynamicdal.dictionary.network.searchSuggestions.model.SearchSuggestionDto
import retrofit2.http.GET
import retrofit2.http.Query

const val METADATA = "dsr"
const val IPA = 1
const val MAX_RESULT = 20

interface WordService {

    // for definition
    @GET("words")
    suspend fun getDefinitions(
        @Query("sl") searchQuery: String,
        @Query("md") metaData: String = METADATA,
        @Query("ipa") ipa: Int = IPA
    ): List<DefinitionDto>

    // for synonyms
    @GET("words")
    suspend fun getSynonyms(
        @Query("ml") searchQuery: String,
        @Query("md") metaData: String = METADATA
    ): List<DefinitionDto>

    // for rhymes
    @GET("words")
    suspend fun getRhymes(
        @Query("rel_rhy") searchQuery: String
    ): List<RhymeDto>

    // for searching
    @GET("sug")
    suspend fun getSearchSuggestions(
        @Query("s") searchQuery: String,
        @Query("max") maxResults: Int = MAX_RESULT
    ): List<SearchSuggestionDto>

}