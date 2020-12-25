package com.haris.houlis.moviemaze.data.source

import androidx.paging.PagingSource
import com.haris.houlis.moviemaze.data.vo.FavoriteAvailability
import com.haris.houlis.moviemaze.data.vo.Movie
import com.haris.houlis.moviemaze.data.vo.MovieRating
import com.haris.houlis.moviemaze.data.vo.ReleaseDate

class FakePagingSource : PagingSource<Int, Movie>() {

    private val movies = mutableListOf(
        Movie(
            1,
            "/sds.jpn",
            "title1",
            ReleaseDate.Unavailable,
            MovieRating.Empty,
            FavoriteAvailability.Unavailable
        )
    )

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> =
        LoadResult.Page(movies, null, null)
}