package com.example.dictionary.network.searchSuggestions.response

import com.example.dictionary.network.searchSuggestions.model.SearchSuggestionDto

data class SearchSuggestionsResponse(

    var searchSuggestions: List<SearchSuggestionDto>
)