package com.example.dictionary.network.searchSuggestions.model

import com.google.gson.annotations.SerializedName

data class SearchSuggestionDto(

    @SerializedName("word")
    var word: String,

    @SerializedName("score")
    var score: Int,
)