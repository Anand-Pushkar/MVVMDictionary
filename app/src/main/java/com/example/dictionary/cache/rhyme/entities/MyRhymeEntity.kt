package com.example.dictionary.cache.rhyme.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * this table holds the words that are starred,
 */
@Entity(tableName = "myRhymes")
class MyRhymeEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "mainWord")
    val mainWord: String,

    @ColumnInfo(name = "syllableInfo")
    val syllableInfo: String,

    @ColumnInfo(name = "isFavorite")
    val isFavorite: Boolean,
)