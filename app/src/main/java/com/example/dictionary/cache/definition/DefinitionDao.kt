package com.example.dictionary.cache.definition

import androidx.room.*
import com.example.dictionary.cache.definition.model.DefinitionEntity
import com.example.dictionary.cache.definition.model.DefinitionEntityMinimal


@Dao
interface DefinitionDao {

    // insert a word in the table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(definition: DefinitionEntity): Long

    // get a particular word from the table
    @Query("SELECT * FROM myWords WHERE word = :word")
    suspend fun getDefinitionByWord(word: String): DefinitionEntity?

    // get a list of all the favorite words with minimal details
    @Query("SELECT word, pronunciation, statement FROM myWords")
    suspend fun getFavoriteList(): List<DefinitionEntityMinimal>?

    // remove a word from the table
    @Delete
    suspend fun deleteWord(word: DefinitionEntity): Int
}