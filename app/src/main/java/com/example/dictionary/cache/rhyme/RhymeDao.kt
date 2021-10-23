package com.example.dictionary.cache.rhyme

import androidx.room.*
import com.example.dictionary.cache.rhyme.entities.MyRhymeEntity
import com.example.dictionary.cache.rhyme.entities.RhymeEntity
import com.example.dictionary.cache.rhyme.relations.RhymeZoneCrossRef
import com.example.dictionary.cache.rhyme.response.RhymeWithAllTheWordsResponse
import com.example.dictionary.cache.rhyme.response.WordWithAllTheRhymesResponse


@Dao
interface RhymeDao {

    // insert favourite rhyme
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMyRhyme(myRhymeEntity: MyRhymeEntity):  Long

    // insert rhyme
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRhyme(rhymeEntity: RhymeEntity): Long

    // insert multiple rhymes
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRhymes(rhymes: List<RhymeEntity>): LongArray

    // inserting relation / cross ref b/w MyRhymeEntity and RhymeEntity
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRhymeZoneCrossRef(crossRef: RhymeZoneCrossRef): Long

    // get all the favourite rhymes
    @Query("SELECT * FROM myRhymes")
    suspend fun getMyRhymes(): List<MyRhymeEntity>

    // get a list of all the rhymes
    @Query("SELECT * FROM rhymes")
    suspend fun getAllRhymes(): List<RhymeEntity>

    // get a word with its list of corresponding rhymes
    @Transaction
    @Query("SELECT * FROM myRhymes WHERE mainWord = :mainWord")
    suspend fun getWordWithAllTheRhymes(mainWord: String): WordWithAllTheRhymesResponse?

    // get rhyme with its list of words it is associated by
    @Transaction
    @Query("SELECT * FROM rhymes WHERE word = :word")
    suspend fun getRhymeWithAllTheWords(word: String): RhymeWithAllTheWordsResponse

    // get all the rhymes with their corresponding words
    @Transaction
    @Query("SELECT * FROM rhymes")
    suspend fun getAllRhymesWithTheirWords(): List<RhymeWithAllTheWordsResponse>

    // delete entry from myRhymes table
    @Delete
    suspend fun deleteMyRhyme(myRhymeEntity: MyRhymeEntity): Int

    // delete entry from cross ref table by mWord
    @Query("DELETE FROM rhymeZoneCrossRef WHERE mainWord = :mainWord")
    suspend fun deleteCrossRefByMWord(mainWord: String): Int

    // delete entry from rhymes table
    @Delete
    suspend fun deleteRhyme(rhymeEntity: RhymeEntity): Int

    // delete multiple entries from rhymes table
    @Delete
    suspend fun deleteManyRhymes(rhymes: List<RhymeEntity>)

}