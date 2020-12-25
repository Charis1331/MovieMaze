package com.haris.houlis.moviemaze.data.source.remote.paging.pagingsource

import androidx.paging.PagingSource
import com.haris.houlis.moviemaze.api.model.response.MoviesResponse
import com.haris.houlis.moviemaze.api.service.TmdbService
import com.haris.houlis.moviemaze.data.mapper.DataMapper
import com.haris.houlis.moviemaze.data.source.remote.paging.mediator.STARTING_PAGE_INDEX
import com.haris.houlis.moviemaze.data.vo.Movie

class SearchMoviesPagingSource(
    private val service: TmdbService,
    private val dataMapper: DataMapper<MoviesResponse, List<Movie>>
) : PagingSource<Int, Movie>() {

    lateinit var query: String

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            getLoadResult(page)
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    private suspend fun getLoadResult(page: Int): LoadResult<Int, Movie> {
        if (!::query.isInitialized) {
            return LoadResult.Error(UninitializedPropertyAccessException("query property is not set!"))
        }

        val result = service.searchMovies(query, page)
        val movies = result.results
        val mappedList = dataMapper.toDomain(result)
        return LoadResult.Page(
            data = mappedList,
            prevKey = if (page == 1) null else page - 1,
            nextKey = if (movies.isEmpty()) null else page + 1
        )
    }
}