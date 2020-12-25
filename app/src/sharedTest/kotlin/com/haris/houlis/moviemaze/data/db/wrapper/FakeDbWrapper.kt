package com.haris.houlis.moviemaze.data.db.wrapper

import androidx.paging.PagingSource
import com.haris.houlis.moviemaze.data.source.FakePagingSource
import com.haris.houlis.moviemaze.data.vo.Movie
import com.haris.houlis.moviemaze.data.vo.RemoteKeys

class FakeDbWrapper : DatabaseWrapper {

    private var favoriteUpdatedRows = 0

    private var remoteKeys: RemoteKeys? = RemoteKeys(1, null, 1)

    override suspend fun <R> withTransaction(block: suspend () -> R): R =
        block()

    override suspend fun insertMovies(movies: List<Movie>) {
    }

    override fun getAllMovies(): PagingSource<Int, Movie> =
        FakePagingSource()

    override suspend fun updateFavorite(movie: Movie): Int = favoriteUpdatedRows

    override suspend fun clearMovies() {
    }

    override suspend fun insertRemoteKeys(remoteKey: List<RemoteKeys>) {
    }

    override suspend fun remoteKeysMovieId(movieId: Int): RemoteKeys? = remoteKeys

    override suspend fun clearRemoteKeys() {
    }

    override suspend fun getFirstRowOfRemoteKeys(): RemoteKeys? = remoteKeys
}