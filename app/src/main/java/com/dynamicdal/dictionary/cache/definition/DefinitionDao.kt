package com.dynamicdal.dictionary.cache.definition

import androidx.room.*
import com.dynamicdal.dictionary.cache.definition.entites.DefinitionEntity
import com.dynamicdal.dictionary.cache.definition.response.DefinitionEntityMinimalResponse


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
    suspend fun getFavoriteList(): List<DefinitionEntityMinimalResponse>?

    // remove a word from the table
    @Delete
    suspend fun deleteWord(word: DefinitionEntity): Int
}