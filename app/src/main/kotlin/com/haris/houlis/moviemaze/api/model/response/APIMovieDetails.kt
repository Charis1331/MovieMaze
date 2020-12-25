package com.haris.houlis.moviemaze.api.model.response

import com.haris.houlis.moviemaze.data.source.DataSource.*

class APIMovieDetailsResponsesWrapper(
    val movieBasicDetailsResponse: MovieBasicDetailsResponse,
    val movieStakeHoldersResponse: MovieStakeHoldersResponse,
    val movieReviewsResult: ResultWrapper<MovieReviewsResponse>,
    val similarMoviesResult: ResultWrapper<SimilarMoviesResponse>
)