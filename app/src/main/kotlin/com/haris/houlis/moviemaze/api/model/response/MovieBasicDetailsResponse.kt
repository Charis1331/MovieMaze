package com.haris.houlis.moviemaze.api.model.response

import com.google.gson.annotations.SerializedName

class MovieBasicDetailsResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("genres")
    val genres: List<APIGenre>,
    @SerializedName("overview")
    val overview: String
)

class APIGenre(
    @SerializedName("name")
    val name: String
)