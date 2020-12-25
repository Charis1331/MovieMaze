@file:Suppress("PrivatePropertyName")

package com.haris.houlis.moviemaze.data.mapper

import com.haris.houlis.moviemaze.api.model.response.*
import com.haris.houlis.moviemaze.data.source.DataSource.ResultWrapper.*
import com.haris.houlis.moviemaze.data.transformer.FakeUrlTransformer
import com.haris.houlis.moviemaze.data.vo.Director
import com.haris.houlis.moviemaze.data.vo.MovieReviews
import com.haris.houlis.moviemaze.data.vo.SimilarMovies
import com.haris.houlis.moviemaze.util.*
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*


class MovieDetailsDataMapperTest {

    private lateinit var SUT: MovieDetailsDataMapper

    @Before
    fun setup() {
        val urlTransformer = FakeUrlTransformer()
        SUT = MovieDetailsDataMapper(urlTransformer)
    }

    @Test
    fun allSuccessfulResponses_toDomain_finalMovieDetailsHasCorrectData() {
        val movieDetails = SUT.toDomain(fakeAPIMovieDetailsResponsesWrapper)

        val actualGenres = movieDetails.getGenres()
        val actualOverview = movieDetails.overview
        val actualCast = movieDetails.getCast()
        val actualDirector = movieDetails.director
        val actualReviews = movieDetails.reviews
        val actualSimilarMovies = movieDetails.similarMovies
        assertTrue(actualGenres == "genre1, genre2")
        assertTrue(actualOverview == "overview")
        assertTrue(actualCast == "cast1, cast2")
        assertTrue((actualDirector as Director.Available).name == "crew1")
        assertTrue(actualReviews is MovieReviews.Available)
        assertTrue(actualSimilarMovies is SimilarMovies.Available)
    }

    @Test
    fun onlyBasicDetailsAndStakeHoldersResponsesAreSuccessful_toDomain_resultIsTheExpectedOnes() {
        val fakeResponseWrapper = APIMovieDetailsResponsesWrapper(
            detailsResponse,
            stakeHoldersResponse,
            Error(),
            Error()
        )

        val movieDetails = SUT.toDomain(fakeResponseWrapper)

        val actualGenres = movieDetails.getGenres()
        val actualOverview = movieDetails.overview
        val actualCast = movieDetails.getCast()
        val actualDirector = movieDetails.director
        val actualReviews = movieDetails.reviews
        val actualSimilarMovies = movieDetails.similarMovies
        assertTrue(actualGenres == "genre1, genre2")
        assertTrue(actualOverview == "overview")
        assertTrue(actualCast == "cast1, cast2")
        assertTrue((actualDirector as Director.Available).name == "crew1")
        assertTrue(actualReviews is MovieReviews.Unavailable)
        assertTrue(actualSimilarMovies is SimilarMovies.Unavailable)
    }

    @Test
    fun movieReviewsResponseFailed_toDomain_resultIsTheExpectedOne() {
        val fakeResponseWrapper = APIMovieDetailsResponsesWrapper(
            detailsResponse,
            stakeHoldersResponse,
            Error(),
            Success(similarMoviesResponse)
        )

        val movieDetails = SUT.toDomain(fakeResponseWrapper)

        val actualGenres = movieDetails.getGenres()
        val actualOverview = movieDetails.overview
        val actualCast = movieDetails.getCast()
        val actualDirector = movieDetails.director
        val actualReviews = movieDetails.reviews
        val actualSimilarMovies = movieDetails.similarMovies
        assertTrue(actualGenres == "genre1, genre2")
        assertTrue(actualOverview == "overview")
        assertTrue(actualCast == "cast1, cast2")
        assertTrue((actualDirector as Director.Available).name == "crew1")
        assertTrue(actualReviews is MovieReviews.Unavailable)
        assertTrue(actualSimilarMovies is SimilarMovies.Available)
    }

    @Test
    fun similarMoviesResponseFailed_toDomain_resultIsTheExpectedOne() {
        val fakeResponseWrapper = APIMovieDetailsResponsesWrapper(
            detailsResponse,
            stakeHoldersResponse,
            Success(reviewsResponse),
            Error()
        )

        val movieDetails = SUT.toDomain(fakeResponseWrapper)

        val actualGenres = movieDetails.getGenres()
        val actualOverview = movieDetails.overview
        val actualCast = movieDetails.getCast()
        val actualDirector = movieDetails.director
        val actualReviews = movieDetails.reviews
        val actualSimilarMovies = movieDetails.similarMovies
        assertTrue(actualGenres == "genre1, genre2")
        assertTrue(actualOverview == "overview")
        assertTrue(actualCast == "cast1, cast2")
        assertTrue((actualDirector as Director.Available).name == "crew1")
        assertTrue(actualReviews is MovieReviews.Available)
        assertTrue(actualSimilarMovies is SimilarMovies.Unavailable)
    }

    @Test
    fun domainReviewsAreMoreThanTwo_toDomain_resultHasOnlyTwoReviews() {
        val movieDetails = SUT.toDomain(fakeAPIMovieDetailsResponsesWrapper)

        val actualReviews = movieDetails.reviews as MovieReviews.Available
        assertTrue(actualReviews.reviews.size == 2)
    }

    @Test
    fun domainSimilarMoviesAreMoreThanTen_toDomain_resultHasOnlyTenReviews() {
        val movieDetails = SUT.toDomain(fakeAPIMovieDetailsResponsesWrapper)

        val actualSimilarMovies = movieDetails.similarMovies as SimilarMovies.Available
        assertTrue(actualSimilarMovies.posters.size == 10)
    }
}