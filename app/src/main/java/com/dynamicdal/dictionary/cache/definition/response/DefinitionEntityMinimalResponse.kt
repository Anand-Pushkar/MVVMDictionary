package com.dynamicdal.dictionary.cache.definition.response

data class DefinitionEntityMinimalResponse(
    val word: String,
    val pronunciation: String,
    val statement: String
)