package com.haris.houlis.moviemaze.api.model.response

import com.google.gson.annotations.SerializedName

class SimilarMoviesResponse(
    @SerializedName("results")
    val similar: List<APISimilarMovie>
)

class APISimilarMovie(
    @SerializedName("poster_path")
    val posterPath: String?
)