package com.haris.houlis.moviemaze.api.model.response

import com.google.gson.annotations.SerializedName

class MovieReviewsResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("results")
    val reviews: List<APIReview>
)

class APIReview(
    @SerializedName("author")
    val author: String,
    @SerializedName("content")
    val content: String
)