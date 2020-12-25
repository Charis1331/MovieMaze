package com.haris.houlis.moviemaze.data.repository

import androidx.paging.PagingData
import com.haris.houlis.moviemaze.data.source.DataSource
import com.haris.houlis.moviemaze.data.vo.Movie
import com.haris.houlis.moviemaze.data.vo.MovieDetails
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    fun getPopularMovies(): Flow<PagingData<Movie>>

     fun searchMovies(newQuery: String): Flow<PagingData<Movie>>

     suspend fun updateFavorite(movie: Movie): Int

     suspend fun getMovieDetails(movie: Movie): DataSource.ResultWrapper<MovieDetails>
}