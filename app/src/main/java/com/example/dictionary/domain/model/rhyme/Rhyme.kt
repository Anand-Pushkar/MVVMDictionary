package com.example.dictionary.domain.model.rhyme

// rhyme business model
data class Rhyme(
    val word: String,
    val score: Int,
    val numSyllables: Int,
)