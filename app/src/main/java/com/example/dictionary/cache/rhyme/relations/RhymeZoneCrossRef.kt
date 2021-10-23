package com.example.dictionary.cache.rhyme.relations

import androidx.room.Entity

@Entity(
    tableName = "rhymeZoneCrossRef",
    primaryKeys = ["mainWord", "word"]
)
data class RhymeZoneCrossRef(
    val mainWord: String,
    val word: String
)