package com.haris.houlis.moviemaze.data.db.typeconverter

import com.haris.houlis.moviemaze.data.vo.FavoriteAvailability
import com.haris.houlis.moviemaze.data.vo.MovieRating
import com.haris.houlis.moviemaze.data.vo.ReleaseDate
import com.haris.houlis.moviemaze.extensions.EMPTY_VALUE_OBJECT
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*

class MovieTypeConverterTest {

    private lateinit var SUT: MovieTypeConverter

    @Before
    fun setup() {
        SUT = MovieTypeConverter()
    }

    @Test
    fun serializedValueIsEmpty_stringToRating_deSerializedValueIsOfTypeEmpty() {
        val serializedValue = EMPTY_VALUE_OBJECT

        val deserializedValue = SUT.stringToRating(serializedValue)

        assertTrue(deserializedValue is MovieRating.Empty)
    }

    @Test
    fun serializedValueIsNotEmpty_stringToRating_resultValueIsTheExpected() {
        val expectedRatingValue = 4.5F
        val serializedValue = "{\"value\": $expectedRatingValue}"

        val deserializedValue = SUT.stringToRating(serializedValue)

        assertTrue((deserializedValue as MovieRating.Available).value == expectedRatingValue)
    }

    @Test
    fun deserializedValueIsOfTypeEmpty_ratingToString_serializedValueIsEmpty() {
        val deserializedValue = MovieRating.Empty

        val serializedValue = SUT.ratingToString(deserializedValue)

        assertTrue(serializedValue == EMPTY_VALUE_OBJECT)
    }

    @Test
    fun deserializedValueIsOfTypeAvailable_ratingToString_serializedValueTheExpected() {
        val expectedValue = 4.5F
        val deserializedValue = MovieRating.Available(expectedValue)

        val actualSerializedValue = SUT.ratingToString(deserializedValue)

        val expectedSerializedValue = "{\"value\":4.5}"
        assertTrue(expectedSerializedValue == actualSerializedValue)
    }

    @Test
    fun serializedValueIsEmpty_stringToFavorite_deSerializedValueIsOfTypeEmpty() {
        val serializedValue = EMPTY_VALUE_OBJECT

        val deserializedValue = SUT.stringToFavoriteSupport(serializedValue)

        assertTrue(deserializedValue is FavoriteAvailability.Unavailable)
    }

    @Test
    fun serializedValueIsAvailable_stringToFavorite_resultValueIsTheExpected() {
        val expectedFavoriteValue = true
        val serializedValue = "{\"isFavorite\":$expectedFavoriteValue}"

        val deserializedValue = SUT.stringToFavoriteSupport(serializedValue)

        assertTrue((deserializedValue as FavoriteAvailability.Available).isFavorite == expectedFavoriteValue)
    }

    @Test
    fun deserializedValueIsOfTypeEmpty_favoriteToString_serializedValueIsEmpty() {
        val deserializedValue = FavoriteAvailability.Unavailable

        val serializedValue = SUT.favoriteSupportToString(deserializedValue)

        assertTrue(serializedValue == EMPTY_VALUE_OBJECT)
    }

    @Test
    fun deserializedValueIsOfTypeAvailable_favoriteToString_serializedValueTheExpected() {
        val expectedValue = true
        val deserializedValue = FavoriteAvailability.Available(expectedValue)

        val actualSerializedValue = SUT.favoriteSupportToString(deserializedValue)

        val expectedSerializedValue = "{\"isFavorite\":true}"
        assertTrue(expectedSerializedValue == actualSerializedValue)
    }

        @Test
    fun serializedValueIsEmpty_stringToReleaseDate_deSerializedValueIsOfTypeEmpty() {
        val serializedValue = EMPTY_VALUE_OBJECT

        val deserializedValue = SUT.stringToReleaseDate(serializedValue)

        assertTrue(deserializedValue is ReleaseDate.Unavailable)
    }

    @Test
    fun serializedValueIsAvailable_stringToReleaseDate_resultValueIsTheExpected() {
        val expectedFavoriteValue = "2 March 2019"
        val serializedValue = "{\"value\":\"$expectedFavoriteValue\"}"

        val deserializedValue = SUT.stringToReleaseDate(serializedValue)

        assertTrue((deserializedValue as ReleaseDate.Available).value == expectedFavoriteValue)
    }

    @Test
    fun deserializedValueIsOfTypeEmpty_releaseDateToString_serializedValueIsEmpty() {
        val deserializedValue = ReleaseDate.Unavailable

        val serializedValue = SUT.releaseDateToString(deserializedValue)

        assertTrue(serializedValue == EMPTY_VALUE_OBJECT)
    }

    @Test
    fun deserializedValueIsOfTypeAvailable_releaseDateToString_serializedValueTheExpected() {
        val expectedValue = "2 March 2019"
        val deserializedValue = ReleaseDate.Available(expectedValue)

        val actualSerializedValue = SUT.releaseDateToString(deserializedValue)

        val expectedSerializedValue = "{\"value\":\"$expectedValue\"}"
        assertTrue(expectedSerializedValue == actualSerializedValue)
    }

}