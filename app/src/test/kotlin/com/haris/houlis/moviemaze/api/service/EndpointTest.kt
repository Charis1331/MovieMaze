package com.haris.houlis.moviemaze.api.service

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.haris.houlis.moviemaze.MainCoroutineRule
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.Timeout
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalCoroutinesApi::class)
class EndpointTest {

    private lateinit var mockServer: MockWebServer

    private lateinit var apiService: TmdbService

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val timeoutRule: Timeout = Timeout.seconds(10)

    @Before
    fun setup() {
        mockServer = MockWebServer()
        mockServer.start()

        val client = OkHttpClient.Builder().build()
        val url = mockServer.url("/").toString()
        apiService = Retrofit.Builder()
            .baseUrl(url)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TmdbService::class.java)
    }

    @After
    fun shutdown() {
        mockServer.shutdown()
    }

    @Test
    fun getPopularMovies_targetsProperEndpoint() {
        mockServer.enqueue(MockResponse().setBody("{}"))

        runBlocking {
            apiService.getPopularMovies(1)
        }

        val request = mockServer.takeRequest()
        assertTrue(request.path == "/3/movie/popular?page=1")
    }

    @Test
    fun searchMovies_targetsProperEndpoint() {
        mockServer.enqueue(MockResponse().setBody("{}"))

        runBlocking {
            apiService.searchMovies("movieTitle", 1)
        }

        val request = mockServer.takeRequest()
        assertTrue(request.path == "/3/search/movie?query=movieTitle&page=1")
    }

    @Test
    fun getMovieBasicDetails_targetsProperEndpoint() {
        mockServer.enqueue(MockResponse().setBody("{}"))

        runBlocking {
            apiService.getMovieBasicDetails(1)
        }

        val request = mockServer.takeRequest()
        assertTrue(request.path == "/3/movie/1")
    }

    @Test
    fun getMovieStakeHolders_targetsProperEndpoint() {
        mockServer.enqueue(MockResponse().setBody("{}"))

        runBlocking {
            apiService.getMovieStakeHolders(1)
        }

        val request = mockServer.takeRequest()
        assertTrue(request.path == "/3/movie/1/credits")
    }

    @Test
    fun getMovieReviews_targetsProperEndpoint() {
        mockServer.enqueue(MockResponse().setBody("{}"))

        runBlocking {
            apiService.getMovieReviews(1)
        }

        val request = mockServer.takeRequest()
        assertTrue(request.path == "/3/movie/1/reviews")
    }

    @Test
    fun getSimilarMovies_targetsProperEndpoint() {
        mockServer.enqueue(MockResponse().setBody("{}"))

        runBlocking {
            apiService.getSimilarMovies(1)
        }

        val request = mockServer.takeRequest()
        assertTrue(request.path == "/3/movie/1/similar")
    }
}