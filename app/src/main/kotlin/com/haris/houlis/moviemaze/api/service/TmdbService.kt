package com.haris.houlis.moviemaze.api.service

import com.haris.houlis.moviemaze.api.model.response.MovieBasicDetailsResponse
import com.haris.houlis.moviemaze.api.model.response.MovieReviewsResponse
import com.haris.houlis.moviemaze.api.model.response.MovieStakeHoldersResponse
import com.haris.houlis.moviemaze.api.model.response.MoviesResponse.PopularMoviesResponse
import com.haris.houlis.moviemaze.api.model.response.MoviesResponse.SearchMoviesResponse
import com.haris.houlis.moviemaze.api.model.response.SimilarMoviesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbService {

    @GET(ENDPOINT_POPULAR_MOVIES)
    suspend fun getPopularMovies(@Query(QUERY_PAGE) page: Int): PopularMoviesResponse

    @GET(ENDPOINT_SEARCH_MOVIES)
    suspend fun searchMovies(
        @Query(QUERY) query: String,
        @Query(QUERY_PAGE) page: Int
    ): SearchMoviesResponse

    @GET(ENDPOINT_MOVIE_DETAILS)
    suspend fun getMovieBasicDetails(@Path(MOVIE_ID) id: Int): MovieBasicDetailsResponse

    @GET(ENDPOINT_CREDIT)
    suspend fun getMovieStakeHolders(@Path(MOVIE_ID) id: Int): MovieStakeHoldersResponse

    @GET(ENDPOINT_REVIEWS)
    suspend fun getMovieReviews(@Path(MOVIE_ID) id: Int): MovieReviewsResponse

    @GET(ENDPOINT_SIMILAR_MOVIES)
    suspend fun getSimilarMovies(@Path(MOVIE_ID) id: Int): SimilarMoviesResponse
}