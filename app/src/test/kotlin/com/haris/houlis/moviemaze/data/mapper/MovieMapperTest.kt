@file:Suppress("PropertyName")

package com.haris.houlis.moviemaze.data.mapper

import com.haris.houlis.moviemaze.data.transformer.FakeUrlTransformer
import com.haris.houlis.moviemaze.data.vo.FavoriteAvailability
import com.haris.houlis.moviemaze.data.vo.MovieRating
import com.haris.houlis.moviemaze.data.vo.ReleaseDate
import com.haris.houlis.moviemaze.util.popularMoviesResponse
import com.haris.houlis.moviemaze.util.searchMoviesResponse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

internal class MovieMapperTest {

    lateinit var SUT: MovieMapper

    @Before
    fun setup() {
        SUT = MovieMapper(FakeUrlTransformer())
    }

    @Test
    fun popularMoviesResponseReturnedThreeMovieObjects_toDomain_resultListHasThreeItems() {
        val movies = SUT.toDomain(popularMoviesResponse)

        assertTrue(movies.size == 3)
    }

    @Test
    fun searchMoviesResponseReturnedThreeMovieObjects_toDomain_resultListHasThreeItems() {
        val movies = SUT.toDomain(searchMoviesResponse)

        assertTrue(movies.size == 3)
    }

    @Test
    fun responseWithObjectThatHasAllValuesFilledAndValid_toDomain_resultItemIsTheExpected() {
        val movies = SUT.toDomain(popularMoviesResponse)

        with(movies[0]) {
            assertTrue(id == 1)
            assertTrue(title == "title1")
            assertTrue((releaseDate as ReleaseDate.Available).value == "22 November 2014")
            assertTrue((rating as MovieRating.Available).value == 1.7f)
            assertTrue(!(favorite as FavoriteAvailability.Available).isFavorite)
        }
    }

    @Test
    fun responseWithObjectThatHasNullReleaseDate_toDomain_resultItemIsTheExpected() {
        val movies = SUT.toDomain(popularMoviesResponse)

        with(movies[1]) {
            assertTrue(id == 2)
            assertTrue(title == "title2")
            assertTrue(releaseDate is ReleaseDate.Unavailable)
            assertTrue((rating as MovieRating.Available).value == 1.7f)
            assertTrue(!(favorite as FavoriteAvailability.Available).isFavorite)
        }
    }

    @Test
    fun responseWithObjectThatHasZeroVoteCount_toDomain_resultItemIsTheExpected() {
        val movies = SUT.toDomain(popularMoviesResponse)

        with(movies[2]) {
            assertTrue(id == 3)
            assertTrue(title == "title3")
            assertTrue((releaseDate as ReleaseDate.Available).value == "22 November 2014")
            assertTrue(rating is MovieRating.Empty)
            assertTrue(!(favorite as FavoriteAvailability.Available).isFavorite)
        }
    }

    @Test
    fun searchMoviesResponse_toDomain_favoriteAvailabilityIsNotAvailableForAllItems() {
        val movies = SUT.toDomain(searchMoviesResponse)

        movies.forEach {
            assertTrue(it.favorite is FavoriteAvailability.Unavailable)
        }
    }
}