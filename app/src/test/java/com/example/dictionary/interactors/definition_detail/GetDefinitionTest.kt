package com.example.dictionary.interactors.definition_detail

import com.example.dictionary.cache.AppDatabaseFake
import com.example.dictionary.cache.DefinitionDaoFake
import com.example.dictionary.cache.definition.mapper.DefinitionEntityMapper
import com.example.dictionary.domain.model.definition.Definition
import com.example.dictionary.interactors.definition_detail_screen.AddToFavoriteWords
import com.example.dictionary.interactors.definition_detail_screen.GetDefinitions
import com.example.dictionary.network.WordService
import com.example.dictionary.network.data.definition.MockWebServerResponseDefinition.definitionDetailResponseInvalid
import com.example.dictionary.network.data.definition.MockWebServerResponseDefinition.definitonDetailResponseForBanana
import com.example.dictionary.network.definition.model.DefinitionDtoMapper
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class GetDefinitionTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl
    private val appDatabase = AppDatabaseFake()
    private val DUMMY_QUERY = "banana"

    // system in test
    private lateinit var getDefinitions: GetDefinitions

    // dependencies
    private lateinit var wordService: WordService
    private val dtoMapper = DefinitionDtoMapper()
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
        mockWebServer = MockWebServer()
        mockWebServer.start()
        baseUrl = mockWebServer.url("")
        wordService = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(WordService::class.java)

        definitionDao = DefinitionDaoFake(appDatabase)


        // instantiate the system in test
        getDefinitions = GetDefinitions(
            dtoMapper = dtoMapper,
            entityMapper = entityMapper,
            wordService = wordService,
            definitionDao = definitionDao
        )
    }

    @Test
    fun foundDefinitionInCacheAndEmit_isFavorite(): Unit = runBlocking {

        // add a word to cache/favorites
        definitionDao.insertWord(entityMapper.mapFromDomainModel(definition))

        // confirm the cache is no longer empty
        assert(definitionDao.getFavoriteWords().isNotEmpty())

        // run use case
        val flowItems = getDefinitions.execute(
            query = DUMMY_QUERY,
            isNetworkAvailable = true
        ).toList()

        // first emission (flowItems[0]) should be LOADING
        assert(flowItems[0].loading)

        // second emission (flowItems[1]) should be Definition object
        val def = flowItems[1].data
        assert(def is Definition)

        // definition object's word should match the query
        assert(def?.word == DUMMY_QUERY)

        // ensure loading is false now
        assert(!flowItems[1].loading)

    }

    @Test
    fun definitionNotInCache_getDefinitionFromNetwork_notFavorite(): Unit = runBlocking {

        // condition the response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(definitonDetailResponseForBanana)
        )

        // confirm the cache is empty to start, no favorite words
        assert(definitionDao.getFavoriteWords().isEmpty())

        // run use case
        val flowItems = getDefinitions.execute(
            query = DUMMY_QUERY,
            isNetworkAvailable = true
        ).toList()

        // first emission (flowItems[0]) should be LOADING
        assert(flowItems[0].loading)

        // second emission (flowItems[1]) should be Definition object
        val def = flowItems[1].data
        assert(def is Definition)

        // ensure loading is false now
        assert(!flowItems[1].loading)

    }

    /**
     * Invalid Search
     */
    @Test
    fun definitionNotInCache_getDefinitionFromNetwork_notFavorite_invalidSearch(): Unit = runBlocking {

        // condition the response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(definitionDetailResponseInvalid)
        )

        // confirm the cache is empty to start, no favorite words
        assert(definitionDao.getFavoriteWords().isEmpty())

        // run use case
        val flowItems = getDefinitions.execute(
            query = DUMMY_QUERY,
            isNetworkAvailable = true
        ).toList()

        // first emission (flowItems[0]) should be LOADING
        assert(flowItems[0].loading)

        // second emission (flowItems[1]) should be error Invalid search
        val error = flowItems[1].error
        assert(error == "Invalid Search")

        // ensure loading is false now
        assert(!flowItems[1].loading)

    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

}