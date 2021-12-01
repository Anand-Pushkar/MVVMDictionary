package com.example.dictionary.interactors.search

import com.example.dictionary.domain.model.searchSuggestion.SearchSuggestion
import com.example.dictionary.interactors.search_screen.UpdateSearchSuggestions
import com.example.dictionary.network.WordService
import com.example.dictionary.network.data.search.MockWebServerResponseSearch
import com.example.dictionary.network.searchSuggestions.model.SearchSuggestionDtoMapper
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

class UpdateSearchSuggestionsTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl
    private val DUMMY_QUERY = "box"

    // system in test
    private lateinit var updateSearchSuggestions: UpdateSearchSuggestions

    // dependencies
    private val dtoMapper = SearchSuggestionDtoMapper()
    private lateinit var wordService: WordService

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

        // instantiate the system in test
        updateSearchSuggestions = UpdateSearchSuggestions(
            dtoMapper = dtoMapper,
            wordService = wordService
        )
    }

    @Test
    fun getSuggestionsFromNetworkAndEmit_notEmpty(): Unit = runBlocking {

        // condition the response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockWebServerResponseSearch.searchResponseForBox)
        )

        // run the use case
        val flowItems = updateSearchSuggestions.execute(
            query = DUMMY_QUERY,
            isNetworkAvailable = true
        ).toList()

        // first emission (flowItems[0]) should be LOADING
        assert(flowItems[0].loading)

        // second emission (flowItems[1]) should be list of suggestions. Check its NOT EMPTY
        val ss = flowItems[1].data
        assert(ss?.size ?: 0 > 0)

        // confirm they are actually searchSuggestions object
        assert(ss?.get(index = 0) is SearchSuggestion)

        // ensure loading is false now
        assert(!flowItems[1].loading)
    }

    @Test
    fun getSuggestionsFromNetworkAndEmit_Empty(): Unit = runBlocking {

        // condition the response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockWebServerResponseSearch.searchResponseInvalidSearch)
        )

        // run the use case
        val flowItems = updateSearchSuggestions.execute(
            query = DUMMY_QUERY,
            isNetworkAvailable = true
        ).toList()

        // first emission (flowItems[0]) should be LOADING
        assert(flowItems[0].loading)

        // second emission (flowItems[1]) should be list of suggestions. Check its EMPTY
        val ss = flowItems[1].data
        assert(ss?.size ?: 0 == 0)

        // ensure loading is false now
        assert(!flowItems[1].loading)
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }
}