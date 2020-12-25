package com.haris.houlis.moviemaze.data.mapper

import com.haris.houlis.moviemaze.api.model.response.APIMovieDetailsResponsesWrapper
import com.haris.houlis.moviemaze.data.vo.*

class FakeMovieDetailsDataMapper : DataMapper<APIMovieDetailsResponsesWrapper, MovieDetails> {

    private var genres = listOf(
        "genre1",
        "genre2"
    )
    private var cast = listOf(
        "cast1",
        "cast2"
    )
    private var director = Director.Available("directorname")
    private var reviews = MovieReviews.Available(
        listOf(
            MovieReview("author1", "content1")
        )
    )
    private var similarMovies = SimilarMovies.Available(
        listOf(
            "/sdsd.jpg",
            "/sdsd.jpg"
        )
    )

    override fun toDomain(source: APIMovieDetailsResponsesWrapper): MovieDetails =
        MovieDetails(
            genres,
            "overview",
            cast,
            director,
            reviews,
            similarMovies
        )
}