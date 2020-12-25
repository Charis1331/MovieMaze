package com.haris.houlis.moviemaze.data.db.wrapper

import androidx.paging.PagingSource
import com.haris.houlis.moviemaze.data.vo.Movie
import com.haris.houlis.moviemaze.data.vo.RemoteKeys

interface DatabaseWrapper {

    suspend fun <R> withTransaction(block: suspend () -> R): R

    suspend fun insertMovies(movies: List<Movie>)

    fun getAllMovies(): PagingSource<Int, Movie>

    suspend fun updateFavorite(movie: Movie): Int

    suspend fun clearMovies()

    suspend fun insertRemoteKeys(remoteKey: List<RemoteKeys>)

    suspend fun remoteKeysMovieId(movieId: Int): RemoteKeys?

    suspend fun clearRemoteKeys()

    suspend fun getFirstRowOfRemoteKeys(): RemoteKeys?
}