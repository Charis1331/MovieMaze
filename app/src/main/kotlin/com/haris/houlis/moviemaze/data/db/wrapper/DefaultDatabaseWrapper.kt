package com.haris.houlis.moviemaze.data.db.wrapper

import androidx.paging.PagingSource
import androidx.room.withTransaction
import com.haris.houlis.moviemaze.data.db.DefaultMoviesDatabase
import com.haris.houlis.moviemaze.data.vo.FavoriteAvailability
import com.haris.houlis.moviemaze.data.vo.Movie
import com.haris.houlis.moviemaze.data.vo.RemoteKeys

class DefaultDatabaseWrapper(private val database: DefaultMoviesDatabase) : DatabaseWrapper {

    override suspend fun <R> withTransaction(block: suspend () -> R): R =
        database.withTransaction {
            block()
        }

    override suspend fun insertMovies(movies: List<Movie>) {
        database.moviesDao().insert(movies)
    }

    override fun getAllMovies(): PagingSource<Int, Movie> = database.moviesDao().allMovies()

    override suspend fun updateFavorite(movie: Movie): Int =
        database.withTransaction {
            val id = movie.id
            val newFavoriteAvailability = getNewFavoriteAvailability(movie)
            database.moviesDao().updateFavorite(id, newFavoriteAvailability)
        }

    private fun getNewFavoriteAvailability(movie: Movie): FavoriteAvailability =
        when (val currentFavoriteAvailability = movie.favorite) {
            FavoriteAvailability.Unavailable -> currentFavoriteAvailability
            is FavoriteAvailability.Available -> FavoriteAvailability.Available(!currentFavoriteAvailability.isFavorite)
        }

    override suspend fun clearMovies() {
        database.moviesDao().clearMovies()
    }

    override suspend fun insertRemoteKeys(remoteKey: List<RemoteKeys>) {
        database.remoteKeysDao().insertAll(remoteKey)
    }

    override suspend fun remoteKeysMovieId(movieId: Int): RemoteKeys? =
        database.remoteKeysDao().remoteKeysMovieId(movieId)

    override suspend fun clearRemoteKeys() {
        database.remoteKeysDao().clearRemoteKeys()
    }

    override suspend fun getFirstRowOfRemoteKeys(): RemoteKeys? =
        database.remoteKeysDao().getFirstRow()
}