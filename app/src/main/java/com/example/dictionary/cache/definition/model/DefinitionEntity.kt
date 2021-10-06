package com.example.dictionary.cache.definition.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "myWords")
class DefinitionEntity(

    // Value from API
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "word")
    val word: String,

    // Value from API
    @ColumnInfo(name = "score")
    val score: Int,

    // Value from API
    @ColumnInfo(name = "numSyllables")
    val numSyllables: Int,

    // Value from API
    @ColumnInfo(name = "tags")
    val tags: String,

    // Value from API
    @ColumnInfo(name = "defs")
    val defs: String,

    // generated value for DefinitionMinimal Model
    @ColumnInfo(name = "pronunciation")
    val pronunciation: String,

    // generated value for DefinitionMinimal Model
    @ColumnInfo(name = "statement")
    val statement: String,

    // generated value for DefinitionMinimal Model
    @ColumnInfo(name = "isFavorite")
    val isFavorite: Boolean,

)

@Entity(tableName = "myWords")
class DefinitionEntityMinimal(
    // Value from API
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "word")
    val word: String,

    // generated value for DefinitionMinimal Model
    @ColumnInfo(name = "pronunciation")
    val pronunciation: String,

    // generated value for DefinitionMinimal Model
    @ColumnInfo(name = "statement")
    val statement: String,
)
