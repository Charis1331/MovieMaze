package com.haris.houlis.moviemaze.util

import com.haris.houlis.moviemaze.api.model.response.*
import com.haris.houlis.moviemaze.api.model.response.MoviesResponse.PopularMoviesResponse
import com.haris.houlis.moviemaze.api.model.response.MoviesResponse.SearchMoviesResponse
import com.haris.houlis.moviemaze.data.source.DataSource.ResultWrapper.Success

val apiMovies = mutableListOf(
    APIMovie(1, "/sds.jpg", "title1", "2014-11-22", 3.4, 2),
    APIMovie(2, "/sds.jpg", "title2", null, 3.4, 2),
    APIMovie(3, "/sds.jpg", "title3", "2014-11-22", 3.4, 0)
)
val popularMoviesResponse = PopularMoviesResponse( apiMovies)
val searchMoviesResponse = SearchMoviesResponse( apiMovies)

val genres = mutableListOf(
    APIGenre("genre1"),
    APIGenre("genre2")
)
val cast = mutableListOf(
    APICast("cast1"),
    APICast("cast2")
)
val crew = mutableListOf(
    APICrew("crew1", "director"),
    APICrew("crew2", "other")
)
val reviews = mutableListOf(
    APIReview("author1", "content1"),
    APIReview("author2", "content2"),
    APIReview("author3", "content3"),
    APIReview("author4", "content4")
)
val similar = mutableListOf(
    APISimilarMovie("/test1.jpg"),
    APISimilarMovie("/test2.jpg"),
    APISimilarMovie("/test2.jpg"),
    APISimilarMovie("/test3.jpg"),
    APISimilarMovie("/test4.jpg"),
    APISimilarMovie("/test5.jpg"),
    APISimilarMovie("/test6.jpg"),
    APISimilarMovie("/test7.jpg"),
    APISimilarMovie("/test8.jpg"),
    APISimilarMovie("/test9.jpg"),
    APISimilarMovie("/test10.jpg"),
    APISimilarMovie("/test11.jpg")
)
val detailsResponse = MovieBasicDetailsResponse(1, genres, "overview")
val stakeHoldersResponse = MovieStakeHoldersResponse(1, cast, crew)
val reviewsResponse = MovieReviewsResponse(1, reviews)
val similarMoviesResponse = SimilarMoviesResponse(similar)
val fakeAPIMovieDetailsResponsesWrapper = APIMovieDetailsResponsesWrapper(
    detailsResponse,
    stakeHoldersResponse,
    Success(reviewsResponse),
    Success(similarMoviesResponse)
)