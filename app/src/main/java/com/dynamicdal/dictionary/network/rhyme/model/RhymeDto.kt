package com.dynamicdal.dictionary.network.rhyme.model

import com.google.gson.annotations.SerializedName

data class RhymeDto(

    @SerializedName("word")
    var word: String,

    @SerializedName("score")
    var score: Int,

    @SerializedName("numSyllables")
    var numSyllables: Int,
)
