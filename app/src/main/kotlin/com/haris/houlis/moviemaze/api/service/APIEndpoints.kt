package com.haris.houlis.moviemaze.api.service

private const val ENDPOINT_API_VERSION = "3/"
private const val ENDPOINT_BASE = ENDPOINT_API_VERSION + "movie/"

const val MOVIE_ID = "movie_id"
const val PATH_MOVE_ID = "{$MOVIE_ID}"

const val ENDPOINT_POPULAR_MOVIES = ENDPOINT_BASE + "popular"
const val ENDPOINT_SEARCH_MOVIES = ENDPOINT_API_VERSION + "search/movie"

const val ENDPOINT_MOVIE_DETAILS = ENDPOINT_BASE + PATH_MOVE_ID
const val ENDPOINT_CREDIT = "$ENDPOINT_MOVIE_DETAILS/credits"
const val ENDPOINT_REVIEWS = "$ENDPOINT_MOVIE_DETAILS/reviews"
const val ENDPOINT_SIMILAR_MOVIES = "$ENDPOINT_MOVIE_DETAILS/similar"