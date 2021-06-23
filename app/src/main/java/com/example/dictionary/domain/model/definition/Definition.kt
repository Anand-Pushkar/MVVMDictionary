package com.example.dictionary.domain.model.definition

// definition business model
data class Definition(
    val word: String,
    val score: Int,
    val numSyllables: Int,
    val tags: List<String>,
    val defs: List<String>
)