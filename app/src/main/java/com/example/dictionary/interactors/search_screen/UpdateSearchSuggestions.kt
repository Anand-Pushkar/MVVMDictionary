package com.example.dictionary.interactors.search_screen

import com.example.dictionary.network.WordService
import com.example.dictionary.network.searchSuggestions.model.SearchSuggestionDtoMapper

class UpdateSearchSuggestion(
    val dtoMapper: SearchSuggestionDtoMapper,
    val wordService: WordService
){
    fun execute(
        query: String
    ){

    }
}