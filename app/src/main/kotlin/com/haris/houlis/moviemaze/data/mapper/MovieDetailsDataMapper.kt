package com.haris.houlis.moviemaze.data.mapper

import com.haris.houlis.moviemaze.api.model.response.*
import com.haris.houlis.moviemaze.data.source.DataSource.ResultWrapper
import com.haris.houlis.moviemaze.data.transformer.UrlTransformer
import com.haris.houlis.moviemaze.data.vo.*

class MovieDetailsDataMapper(private val posterUrlTransformer: UrlTransformer) :
    DataMapper<APIMovieDetailsResponsesWrapper, MovieDetails> {

    override fun toDomain(source: APIMovieDetailsResponsesWrapper): MovieDetails {
        with(source) {
            val genres = getGenres()
            val overview = movieBasicDetailsResponse.overview
            val cast = getCast()
            val director = getDirector()

            val reviews: MovieReviews = getReviews()
            val similarMovies = getSimilarMovies()
            return MovieDetails(
                genres,
                overview,
                cast,
                director,
                reviews,
                similarMovies
            )
        }
    }

    private fun APIMovieDetailsResponsesWrapper.getGenres(): List<String> =
        movieBasicDetailsResponse.genres.map {
            it.name
        }

    private fun APIMovieDetailsResponsesWrapper.getCast(): List<String> =
        movieStakeHoldersResponse.cast.map {
            it.name
        }

    private fun APIMovieDetailsResponsesWrapper.getDirector(): Director {
        movieStakeHoldersResponse.getDirectorObject()
            ?.let {
                return Director.Available(it.name)
            }
        return Director.Empty
    }

    private fun MovieStakeHoldersResponse.getDirectorObject(): APICrew? =
        crew.firstOrNull {
            it.job.equals(DIRECTOR_JOB_NAME, true)
        }

    private fun APIMovieDetailsResponsesWrapper.getReviews(): MovieReviews =
        if (movieReviewsResult is ResultWrapper.Success && movieReviewsResult.data != null) {
            val reviews = movieReviewsResult.data.getDomainReviewsList()
            MovieReviews.Available(reviews)
        } else {
            MovieReviews.Unavailable
        }

    private fun MovieReviewsResponse.getDomainReviewsList(): List<MovieReview> =
        reviews.take(NUMBER_OF_REVIEWS_TO_STORE)
            .map {
                MovieReview(it.author, it.content)
            }

    private fun APIMovieDetailsResponsesWrapper.getSimilarMovies(): SimilarMovies =
        if (similarMoviesResult is ResultWrapper.Success && similarMoviesResult.data != null) {
            val similarMovies = similarMoviesResult.data.getDomainSimilarMoviesList()
            SimilarMovies.Available(similarMovies)
        } else {
            SimilarMovies.Unavailable
        }

    private fun SimilarMoviesResponse.getDomainSimilarMoviesList(): List<String> =
        similar.take(NUMBER_OF_SIMILAR_MOVIES_TO_STORE)
            .filter {
                !it.posterPath.isNullOrBlank()
            }.map {
                posterUrlTransformer.getTransformedUrl(it.posterPath)
            }

    private companion object {
        private const val DIRECTOR_JOB_NAME = "director"
        private const val NUMBER_OF_REVIEWS_TO_STORE = 2
        private const val NUMBER_OF_SIMILAR_MOVIES_TO_STORE = 10
    }
}