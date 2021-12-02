package com.dynamicdal.dictionary.cache.rhyme.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * this table holds the rhymes. this resembles our domain model Rhyme.kt
 */
@Entity(tableName = "rhymes")
data class RhymeEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "word")
    val word: String,

    @ColumnInfo(name = "score")
    val score: Int,

    @ColumnInfo(name = "numSyllables")
    val numSyllables: Int,
)



