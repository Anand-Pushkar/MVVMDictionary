package com.example.dictionary.network.definition.model

import com.google.gson.annotations.SerializedName

data class DefinitionDto(

    @SerializedName("word")
    var word: String,

    @SerializedName("score")
    var score: Int,

    @SerializedName("numSyllables")
    var numSyllables: Int,

    @SerializedName("tags")
    var tags: List<String>,

    @SerializedName("defs")
    var defs: List<String>?
)