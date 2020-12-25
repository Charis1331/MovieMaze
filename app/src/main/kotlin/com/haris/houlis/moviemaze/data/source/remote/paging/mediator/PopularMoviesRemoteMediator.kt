package com.haris.houlis.moviemaze.data.source.remote.paging.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.*
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.paging.RemoteMediator.InitializeAction.LAUNCH_INITIAL_REFRESH
import androidx.paging.RemoteMediator.InitializeAction.SKIP_INITIAL_REFRESH
import com.haris.houlis.moviemaze.api.model.response.MoviesResponse
import com.haris.houlis.moviemaze.api.model.response.MoviesResponse.PopularMoviesResponse
import com.haris.houlis.moviemaze.api.service.TmdbService
import com.haris.houlis.moviemaze.data.db.wrapper.DatabaseWrapper
import com.haris.houlis.moviemaze.data.mapper.DataMapper
import com.haris.houlis.moviemaze.data.vo.Movie
import com.haris.houlis.moviemaze.data.vo.RemoteKeys

const val STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class PopularMoviesRemoteMediator(
    private val service: TmdbService,
    private val databaseWrapper: DatabaseWrapper,
    private val dataMapper: DataMapper<MoviesResponse, List<Movie>>
) : RemoteMediator<Int, Movie>() {

    override suspend fun initialize(): InitializeAction =
        if (isDbEmpty()) {
            LAUNCH_INITIAL_REFRESH
        } else {
            SKIP_INITIAL_REFRESH
        }

    private suspend fun isDbEmpty(): Boolean =
        getFirstRemoteKeyOrNull() == null

    private suspend fun getFirstRemoteKeyOrNull(): RemoteKeys? =
        databaseWrapper.getFirstRowOfRemoteKeys()

    @Suppress("FoldInitializerAndIfToElvis")
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Movie>): MediatorResult {
        try {
            val loadKey = when (loadType) {
                REFRESH -> STARTING_PAGE_INDEX
                PREPEND -> return MediatorResult.Success(true)
                APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    if (remoteKeys?.nextKey == null) {
                        return MediatorResult.Success(true)
                    }
                    remoteKeys.nextKey
                }
            }

            val moviesResponse = service.getPopularMovies(loadKey)
            return getMediatorResult(moviesResponse, loadType, loadKey)
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Movie>): RemoteKeys? {
        val lastMovieInLatestData = getLastMovieInLatestData(state)
        if (lastMovieInLatestData != null) {
            return databaseWrapper.remoteKeysMovieId(lastMovieInLatestData.id)
        }
        return null
    }

    private fun getLastMovieInLatestData(state: PagingState<Int, Movie>): Movie? {
        val lastPageWithData = state.pages.lastOrNull { it.data.isNotEmpty() }
        val lastPageData = lastPageWithData?.data
        return lastPageData?.lastOrNull()
    }

    private suspend fun getMediatorResult(
        source: PopularMoviesResponse,
        loadType: LoadType,
        page: Int
    ): MediatorResult {
        val apiMovies = source.results
        val endOfPaginationReached = apiMovies.isEmpty()
        updateDatabase(loadType, page, endOfPaginationReached, source)

        return MediatorResult.Success(endOfPaginationReached)
    }

    private suspend fun updateDatabase(
        loadType: LoadType,
        page: Int,
        endOfPaginationReached: Boolean,
        source: PopularMoviesResponse
    ) {
        val domainMoviesList = dataMapper.toDomain(source)
        databaseWrapper.withTransaction {
            cleanDbOnRefresh(loadType)

            insertNewDataInDb(page, endOfPaginationReached, domainMoviesList)
        }
    }

    private suspend fun cleanDbOnRefresh(loadType: LoadType) {
        if (loadType == REFRESH) {
            with(databaseWrapper) {
                clearRemoteKeys()
                clearMovies()
            }
        }
    }

    private suspend fun insertNewDataInDb(
        page: Int,
        endOfPaginationReached: Boolean,
        domainMoviesList: List<Movie>
    ) {
        insertToRemoteKeysTable(page, endOfPaginationReached, domainMoviesList)

        databaseWrapper.insertMovies(domainMoviesList)
    }

    private suspend fun insertToRemoteKeysTable(
        page: Int,
        endOfPaginationReached: Boolean,
        domainMoviesList: List<Movie>
    ) {
        val keys = getRemoteKeys(page, endOfPaginationReached, domainMoviesList)
        databaseWrapper.insertRemoteKeys(keys)
    }

    private fun getRemoteKeys(
        page: Int,
        endOfPaginationReached: Boolean,
        domainMoviesList: List<Movie>
    ): List<RemoteKeys> {
        val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
        val nextKey = if (endOfPaginationReached) null else page + 1
        return domainMoviesList.map {
            RemoteKeys(it.id, prevKey, nextKey)
        }
    }
}