package com.example.dictionary.interactors.definition_detail

import com.example.dictionary.cache.AppDatabaseFake
import com.example.dictionary.cache.DefinitionDaoFake
import com.example.dictionary.cache.definition.mapper.DefinitionEntityMapper
import com.example.dictionary.domain.model.definition.Definition
import com.example.dictionary.interactors.definition_detail_screen.AddToFavoriteWords
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AddToFavoriteWordsTest {

    private val appDatabase = AppDatabaseFake()
    private val DUMMY_QUERY = "banana"

    // system in test
    private lateinit var addToFavoriteWords: AddToFavoriteWords

    // dependencies
    private val entityMapper =  DefinitionEntityMapper()
    private lateinit var definitionDao: DefinitionDaoFake
    private val definition = Definition(
        word = "banana",
        score = 100,
        numSyllables = 3,
        tags = listOf("pron:B AH0 N AE1 N AH0 ",
            "ipa_pron:bʌnˈænʌ"),
        defs = listOf("n\telongated crescent-shaped yellow fruit with soft sweet flesh",
            "n\tany of several tropical and subtropical treelike herbs of the genus Musa having a terminal crown of large entire leaves and usually bearing hanging clusters of elongated fruits"),
        isFavorite = false
    )

    @BeforeEach
    fun setup() {

        definitionDao = DefinitionDaoFake(appDatabase)

        // instantiate the system in test
        addToFavoriteWords = AddToFavoriteWords(
            entityMapper = entityMapper,
            definitionDao = definitionDao
        )
    }

    @Test
    fun insertIntoCache_readFromCache_emit(): Unit = runBlocking {

        // confirm the cache is empty to start, no favorite words
        assert(definitionDao.getFavoriteWords().isEmpty())

        // run use case
        val flowItems = addToFavoriteWords.execute(definition).toList()

        // confirm the cache is no longer empty
        assert(definitionDao.getFavoriteWords().isNotEmpty())

        // first emission (flowItems[0]) should be LOADING
        assert(flowItems[0].loading)

        // second emission (flowItems[1]) should be Definition object
        val def = flowItems[1].data
        assert(def is Definition)

        // check isFavorite is true
        assert(def?.isFavorite == true)

        // ensure loading is false now
        assert(!flowItems[1].loading)
    }



}