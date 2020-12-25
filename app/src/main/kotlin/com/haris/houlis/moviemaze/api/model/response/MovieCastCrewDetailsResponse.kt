package com.haris.houlis.moviemaze.api.model.response

import com.google.gson.annotations.SerializedName

class MovieStakeHoldersResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("cast")
    val cast: List<APICast>,
    @SerializedName("crew")
    val crew: List<APICrew>
)

class APICast(
    @SerializedName("name")
    val name: String
)

class APICrew(
    @SerializedName("name")
    val name: String,
    @SerializedName("job")
    val job: String?
)