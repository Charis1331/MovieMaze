package com.haris.houlis.moviemaze.data.source

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.haris.houlis.moviemaze.MainCoroutineRule
import com.haris.houlis.moviemaze.api.service.FakeApiService
import com.haris.houlis.moviemaze.data.mapper.FakeMovieDetailsDataMapper
import com.haris.houlis.moviemaze.data.source.DataSource.ResultWrapper
import com.haris.houlis.moviemaze.data.source.remote.MovieDetailsRemoteDataSource
import com.haris.houlis.moviemaze.data.source.remote.UNKNOWN_ERROR_CODE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.IOException
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailsRemoteDataSourceTest {

    private val service = FakeApiService()
    private val dispatchers = Dispatchers.Main

    private lateinit var SUT: MovieDetailsRemoteDataSource

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        val dataMapper = FakeMovieDetailsDataMapper()
        SUT = MovieDetailsRemoteDataSource(service, dataMapper, dispatchers)
    }

    @Test
    fun getMovieDetails_allRequestsAreSuccessFull_resultIsSuccessful() =
        mainCoroutineRule.runBlockingTest {
            resetAllExceptions()

            val result = SUT.getMovieDetails(1)

            assertTrue(result is ResultWrapper.Success)
        }

    @Test
    fun getMovieDetails_allRequestsFail_resultIsError() = mainCoroutineRule.runBlockingTest {
        resetAllExceptions()
        service.exception = IOException()

        val result = SUT.getMovieDetails(1)

        assertTrue(result is ResultWrapper.Error)
    }

    @Test
    fun getMovieDetails_basicDetailsRequestFails_resultIsOfTypeError() =
        mainCoroutineRule.runBlockingTest {
            resetAllExceptions()
            service.basicDetailsException = IOException()

            val result = SUT.getMovieDetails(1)

            assertTrue(result is ResultWrapper.Error)
        }

    @Test
    fun getMovieDetails_stakeHoldersRequestFails_resultIsOfTypeError() =
        mainCoroutineRule.runBlockingTest {
            resetAllExceptions()
            service.stakeholdersException = IOException()

            val result = SUT.getMovieDetails(1)

            assertTrue(result is ResultWrapper.Error)
        }

    @Test
    fun getMovieDetails_reviewsRequestFails_resultIsOfTypeSuccess() =
        mainCoroutineRule.runBlockingTest {
            resetAllExceptions()
            service.reviewsException = IOException()

            val result = SUT.getMovieDetails(1)

            assertTrue(result is ResultWrapper.Success)
        }

    @Test
    fun getMovieDetails_similarMoviesRequestFails_resultIsOfTypeSuccess() =
        mainCoroutineRule.runBlockingTest {
            resetAllExceptions()
            service.similarException = IOException()

            val result = SUT.getMovieDetails(1)

            assertTrue(result is ResultWrapper.Success)
        }

    @Test
    fun safeApiCall_returnsSuccessfully_resultIsOfTypeSuccess() {
        runBlockingTest {
            val expectedResult = true
            val result = SUT.safeApiCall(dispatchers) {
                expectedResult
            }

            assertTrue((result as ResultWrapper.Success).data == expectedResult)
        }
    }

    @Test
    fun safeApiCall_throwsIllegalStateException_resultIsOfTypeError() =
        mainCoroutineRule.runBlockingTest {
            val result = SUT.safeApiCall(dispatchers) {
                throw IllegalStateException()
            }

            assertTrue(result is ResultWrapper.Error)
        }

    @Test
    fun safeApiCall_throwsIOException_resultIsOfTypeNetworkError() =
        mainCoroutineRule.runBlockingTest {
            val result = SUT.safeApiCall(dispatchers) {
                throw IOException()
            }

            assertTrue(result is ResultWrapper.NetworkError)
        }

    @Test
    fun safeApiCall_throwsHttpException_resultErrorCodeIsTheExpected() =
        mainCoroutineRule.runBlockingTest {
            val errorCode = 405
            val errorBody = "{}".toResponseBody("application/json".toMediaTypeOrNull())
            val result = SUT.safeApiCall(dispatchers) {
                throw HttpException(Response.error<Any>(errorCode, errorBody))
            }

            assertTrue((result as ResultWrapper.Error).code == errorCode)
        }

    @Test
    fun safeApiCall_throwsIllegalStateException_errorCodeIsUnknown() =
        mainCoroutineRule.runBlockingTest {
            val result = SUT.safeApiCall(dispatchers) {
                throw IllegalStateException()
            }

            assertTrue((result as ResultWrapper.Error).code == UNKNOWN_ERROR_CODE)
        }

    private fun resetAllExceptions() {
        with(service) {
            exception = null
            basicDetailsException = null
            stakeholdersException = null
            reviewsException = null
            similarException = null
        }
    }
}