package com.example.dictionary.cache

import com.example.dictionary.cache.rhyme.RhymeDao
import com.example.dictionary.cache.rhyme.entities.MyRhymeEntity
import com.example.dictionary.cache.rhyme.entities.RhymeEntity
import com.example.dictionary.cache.rhyme.relations.RhymeZoneCrossRef
import com.example.dictionary.cache.rhyme.response.RhymeWithAllTheWordsResponse
import com.example.dictionary.cache.rhyme.response.WordWithAllTheRhymesResponse

class RhymeDaoFake(
    private val appDatabaseFake: AppDatabaseFake
): RhymeDao {
    override suspend fun insertMyRhyme(myRhymeEntity: MyRhymeEntity): Long {
        appDatabaseFake.myRhymes.add(myRhymeEntity)
        return 1 // success
    }

    override suspend fun insertRhyme(rhymeEntity: RhymeEntity): Long {
        appDatabaseFake.rhymes.add(rhymeEntity)
        return 1 // success
    }

    override suspend fun insertRhymes(rhymes: List<RhymeEntity>): LongArray {
        appDatabaseFake.rhymes.addAll(rhymes)
        return longArrayOf(1) // success
    }

    override suspend fun insertRhymeZoneCrossRef(crossRef: RhymeZoneCrossRef): Long {
        appDatabaseFake.crossRef.add(crossRef)
        return 1 // success
    }

    override suspend fun getMyRhymes(): List<MyRhymeEntity>? {
        return appDatabaseFake.myRhymes
    }

    override suspend fun getAllRhymes(): List<RhymeEntity> {
        return appDatabaseFake.rhymes
    }

    override suspend fun getWordWithAllTheRhymes(mainWord: String): WordWithAllTheRhymesResponse? {
        return WordWithAllTheRhymesResponse(
            myRhyme = appDatabaseFake.myRhymes.get(0),
            relatedRhymes = appDatabaseFake.rhymes
        )
    }

    override suspend fun getRhymeWithAllTheWords(word: String): RhymeWithAllTheWordsResponse {
        return RhymeWithAllTheWordsResponse(
            rhyme = appDatabaseFake.rhymes.get(0),
            cWords = appDatabaseFake.myRhymes
        )
    }

    override suspend fun getAllRhymesWithTheirWords(): List<RhymeWithAllTheWordsResponse> {
        return listOf(
            RhymeWithAllTheWordsResponse(
                rhyme = appDatabaseFake.rhymes.get(0),
                cWords = appDatabaseFake.myRhymes
            )
        )
    }

    override suspend fun deleteMyRhyme(myRhymeEntity: MyRhymeEntity): Int {
        appDatabaseFake.myRhymes.remove(myRhymeEntity)
        return 1 // success
    }

    override suspend fun deleteCrossRefByMWord(mainWord: String): Int {
        appDatabaseFake.crossRef.removeIf {
            it.mainWord == mainWord
        }
        return 1 // success
    }

    override suspend fun deleteRhyme(rhymeEntity: RhymeEntity): Int {
        appDatabaseFake.rhymes.remove(rhymeEntity)
        return 1 // success
    }

    override suspend fun deleteManyRhymes(rhymes: List<RhymeEntity>) {
        appDatabaseFake.rhymes.removeAll(rhymes)
    }
}