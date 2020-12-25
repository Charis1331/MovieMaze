package com.haris.houlis.moviemaze.api.service

import com.haris.houlis.moviemaze.api.model.response.*

class FakeApiService : TmdbService {

    var exception: Exception? = null
    var basicDetailsException: Exception? = null
    var stakeholdersException: Exception? = null
    var reviewsException: Exception? = null
    var similarException: Exception? = null

    private val apiMovies = mutableListOf(
        APIMovie(1, "/27zvjVOtOi5ped1HSlJKNsKXkFH.jpg", "title1", "2014-11-22", 3.4, 2),
        APIMovie(2, null, "title1", null, 3.4, 0)
    )

    private val apiMoviesForSearchCase = mutableListOf(
        APIMovie(1, "/sds.jpg", "title1", "2014-11-22", 3.4, 2),
        APIMovie(2, "/sds.jpg", "title2", null, 3.4, 0),
        APIMovie(3, "/sds.jpg", "title3", null, 3.4, 3),
        APIMovie(4, null, "title4", null, 3.4, 0)
    )

    private val genres = mutableListOf(
        APIGenre("genre1"),
        APIGenre("genre2")
    )
    private val cast = mutableListOf(
        APICast("cast1"),
        APICast("cast2")
    )
    private val crew = mutableListOf(
        APICrew("cast1", "director"),
        APICrew("cast2", "other")
    )
    private val reviews = mutableListOf(
        APIReview("author1", "content1")
    )
    private val similar = mutableListOf(
        APISimilarMovie("/test.jpg"),
        APISimilarMovie("/test.jpg")
    )

    override suspend fun getPopularMovies(page: Int): MoviesResponse.PopularMoviesResponse {
        exception?.let {
            throw it
        }

        if (page != 1) return MoviesResponse.PopularMoviesResponse(emptyList())

        return MoviesResponse.PopularMoviesResponse(apiMovies)
    }

    override suspend fun searchMovies(
        query: String,
        page: Int
    ): MoviesResponse.SearchMoviesResponse {
        exception?.let {
            throw it
        }

        if (page != 1) return MoviesResponse.SearchMoviesResponse(emptyList())

        return MoviesResponse.SearchMoviesResponse(apiMoviesForSearchCase)
    }

    override suspend fun getMovieBasicDetails(id: Int): MovieBasicDetailsResponse {
        exception?.let {
            throw it
        }

        basicDetailsException?.let {
            throw it
        }

        return MovieBasicDetailsResponse(1, genres, "overview")
    }

    override suspend fun getMovieStakeHolders(id: Int): MovieStakeHoldersResponse {
        exception?.let {
            throw it
        }

        stakeholdersException?.let {
            throw it
        }

        return MovieStakeHoldersResponse(1, cast, crew)
    }


    override suspend fun getMovieReviews(id: Int): MovieReviewsResponse {
        exception?.let {
            throw it
        }

        reviewsException?.let {
            throw it
        }

        return MovieReviewsResponse(1, reviews)
    }

    override suspend fun getSimilarMovies(id: Int): SimilarMoviesResponse {
        exception?.let {
            throw it
        }

        similarException?.let {
            throw it
        }

        return SimilarMoviesResponse(similar)
    }
}