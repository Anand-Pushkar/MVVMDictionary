package com.example.dictionary.cache

import com.example.dictionary.cache.definition.DefinitionDao
import com.example.dictionary.cache.definition.entites.DefinitionEntity
import com.example.dictionary.cache.definition.response.DefinitionEntityMinimalResponse

class DefinitionDaoFake(
    private val appDatabaseFake: AppDatabaseFake
): DefinitionDao {
    override suspend fun insertWord(definition: DefinitionEntity): Long {
        appDatabaseFake.myWords.add(definition)
        return 1 // success
    }

    override suspend fun getDefinitionByWord(word: String): DefinitionEntity? {
        return appDatabaseFake.myWords.find { it.word == word }
    }

    suspend fun getFavoriteWords(): List<DefinitionEntity> {
        return appDatabaseFake.myWords
    }

    override suspend fun getFavoriteList(): List<DefinitionEntityMinimalResponse>? {
        TODO()
    }

    override suspend fun deleteWord(word: DefinitionEntity): Int {
        appDatabaseFake.myWords.remove(word)
        return 1 // success
    }

}