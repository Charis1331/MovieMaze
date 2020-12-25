package com.haris.houlis.moviemaze.api.model.response

import com.google.gson.annotations.SerializedName

sealed class MoviesResponse(
    @SerializedName("results") val results: List<APIMovie>
) {
    class PopularMoviesResponse(results: List<APIMovie>) : MoviesResponse(results)
    class SearchMoviesResponse( results: List<APIMovie>) : MoviesResponse(results)
}

class APIMovie(
    @SerializedName("id")
    val id: Int,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("title")
    val title: String,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("vote_average")
    val rating: Double,
    @SerializedName("vote_count")
    val voteCount: Int
)