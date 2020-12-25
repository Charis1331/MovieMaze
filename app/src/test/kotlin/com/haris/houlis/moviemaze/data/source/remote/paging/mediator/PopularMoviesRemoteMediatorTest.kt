package com.haris.houlis.moviemaze.data.source.remote.paging.mediator

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.*
import androidx.paging.RemoteMediator.MediatorResult
import com.haris.houlis.moviemaze.MainCoroutineRule
import com.haris.houlis.moviemaze.api.service.FakeApiService
import com.haris.houlis.moviemaze.data.db.wrapper.FakeDbWrapper
import com.haris.houlis.moviemaze.data.mapper.FakePopularMoviesMapper
import com.haris.houlis.moviemaze.data.vo.Movie
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PopularMoviesRemoteMediatorTest {

    private val service = FakeApiService()

    private lateinit var SUT: PopularMoviesRemoteMediator

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        val databaseWrapper = FakeDbWrapper()
        val dataMapper = FakePopularMoviesMapper()
        SUT = PopularMoviesRemoteMediator(service, databaseWrapper, dataMapper)
    }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun onRefreshServiceCallSucceeded_load_resultIsOfTypeSuccess() =
        mainCoroutineRule.runBlockingTest {
            service.exception = null
            val loadType = LoadType.REFRESH

            val mediatorResult = SUT.load(loadType, getFakePagingState())

            assertTrue(mediatorResult is MediatorResult.Success)
        }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun onRefreshServiceCallFailed_load_resultIsOfTypeSuccess() =
        mainCoroutineRule.runBlockingTest {
            service.exception = Exception()
            val loadType = LoadType.REFRESH

            val mediatorResult = SUT.load(loadType, getFakePagingState())

            assertTrue(mediatorResult is MediatorResult.Error)
        }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun onPrepend_load_resultIsOfTypeSuccessAndEndOfPagination() =
        mainCoroutineRule.runBlockingTest {
            val loadType = LoadType.PREPEND

            val mediatorResult = SUT.load(loadType, getFakePagingState())

            assertTrue((mediatorResult as MediatorResult.Success).endOfPaginationReached)
        }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun onAppendAndNextKeyIsNull_load_resultIsOfTypeSuccessAndEndOfPagination() =
        mainCoroutineRule.runBlockingTest {
            val loadType = LoadType.APPEND

            val mediatorResult = SUT.load(loadType, getFakePagingState())

            assertTrue((mediatorResult as MediatorResult.Success).endOfPaginationReached)
        }

    private fun getFakePagingState(): PagingState<Int, Movie> {
        val pages = listOf<PagingSource.LoadResult.Page<Int, Movie>>()
        val pagingConfig = PagingConfig(10)
        return PagingState(pages, null, pagingConfig, 0)
    }
}