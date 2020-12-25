package com.haris.houlis.moviemaze.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.haris.houlis.moviemaze.data.db.wrapper.DatabaseWrapper
import com.haris.houlis.moviemaze.data.source.DataSource
import com.haris.houlis.moviemaze.data.source.DataSourcesWrapper
import com.haris.houlis.moviemaze.data.vo.Movie
import com.haris.houlis.moviemaze.data.vo.MovieDetails
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultMoviesRepository(
    private val databaseWrapper: DatabaseWrapper,
    private val dataSourcesWrapper: DataSourcesWrapper
) : MoviesRepository {

    override fun getPopularMovies(): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(pageSize = DATABASE_PAGE_SIZE, enablePlaceholders = false),
        remoteMediator = dataSourcesWrapper.pagingSources.popularMoviesRemoteMediator
    ) {
        databaseWrapper.getAllMovies()
    }.flow


    override fun searchMovies(newQuery: String): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(pageSize = DATABASE_PAGE_SIZE, enablePlaceholders = false)
    ) {
        dataSourcesWrapper.pagingSources.searchMoviesPagingSource.apply {
            query = newQuery
        }
    }.flow

    override suspend fun updateFavorite(movie: Movie): Int =
        databaseWrapper.updateFavorite(movie)

    override suspend fun getMovieDetails(movie: Movie): DataSource.ResultWrapper<MovieDetails> =
        dataSourcesWrapper.movieDetailsSource.getMovieDetails(movie.id)

    companion object {
        private const val DATABASE_PAGE_SIZE = 10
    }
}