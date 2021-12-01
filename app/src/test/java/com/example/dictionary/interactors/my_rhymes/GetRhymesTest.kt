package com.example.dictionary.interactors.my_rhymes

import com.example.dictionary.cache.AppDatabaseFake
import com.example.dictionary.cache.RhymeDaoFake
import com.example.dictionary.cache.rhyme.entities.MyRhymeEntity
import com.example.dictionary.cache.rhyme.mapper.RhymeEntityMapper
import com.example.dictionary.domain.model.rhyme.Rhyme
import com.example.dictionary.domain.model.rhyme.Rhymes
import com.example.dictionary.interactors.rhyme_detail_screen.GetRhymes
import com.example.dictionary.network.WordService
import com.example.dictionary.network.data.rhyme.MockWebServerResponseRhyme.rhymeDetailResponseForYo
import com.example.dictionary.network.rhyme.model.RhymeDtoMapper
import com.example.dictionary.presentation.ui.myRhymes.MyRhyme
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

@ExperimentalStdlibApi
class GetRhymesTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl
    private val appDatabase = AppDatabaseFake()
    private val DUMMY_QUERY = "yo"

    // system in test
    private lateinit var getRhymes: GetRhymes

    // dependencies
    private lateinit var wordService: WordService
    private val dtoMapper = RhymeDtoMapper()
    private val entityMapper =  RhymeEntityMapper()
    private lateinit var rhymeDao: RhymeDaoFake


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

        rhymeDao = RhymeDaoFake(appDatabase)


        // instantiate the system in test
        getRhymes = GetRhymes(
            dtoMapper = dtoMapper,
            entityMapper = entityMapper,
            wordService = wordService,
            rhymeDao = rhymeDao
        )
    }

    @Test
    fun foundRhymeInCacheAndEmit_isFavorite(): Unit = runBlocking {
        // add a rhyme to cache/favorites

    }

    @Test
    fun rhymeNotInCache_getRhymeFromNetwork_notFavorite(): Unit = runBlocking {

        // condition the response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(rhymeDetailResponseForYo)
        )

        // confirm the cache is empty to start, no favorite words
        assert(rhymeDao.getAllRhymes().isEmpty())

        // run use case
        val flowItems = getRhymes.execute(
            query = DUMMY_QUERY,
            isNetworkAvailable = true
        ).toList()

        // first emission (flowItems[0]) should be LOADING
        assert(flowItems[0].loading)

        // second emission (flowItems[1]) should be Rhymes object
        val rhymes = flowItems[1].data
        assert(rhymes is Rhymes)
        //assert(flowItems[1].error == null)

        // ensure loading is false now
        assert(!flowItems[1].loading)
    }


    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }
}