package com.haris.houlis.moviemaze.data.transformer

import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PosterUrlTransformerTest {

    lateinit var SUT: PosterUrlTransformer

    @Before
    fun setup() {
        SUT = PosterUrlTransformer()
    }

    @Test
    fun validInitialPath_getTransformedUrl_finalUrlIsTheExpectedOne() {
        val initialUrl = "/testPath.jpg"

        val transformedUrl = SUT.getTransformedUrl(initialUrl)

        val expectedUrl = "http://image.tmdb.org/t/p/w500/testPath.jpg"
        assertTrue(transformedUrl == expectedUrl)
    }

    @Test
    fun initialUrlIsEmpty_getTransformedUrl_finalUrlIsEmpty() {
        val initialUrl = ""

        val transformedUrl = SUT.getTransformedUrl(initialUrl)

        assertTrue(transformedUrl.isEmpty())
    }

    @Test
    fun initialUrlIsBlank_getTransformedUrl_finalUrlIsEmpty() {
        val initialUrl = "   "

        val transformedUrl = SUT.getTransformedUrl(initialUrl)

        assertTrue(transformedUrl.isEmpty())
    }

    @Test
    fun initialUrlIsNotBlankButHasWhiteSpace_getTransformedUrl_finalUrlIsEmpty() {
        val initialUrl = "/testPa th.jpg"

        val transformedUrl = SUT.getTransformedUrl(initialUrl)

        assertTrue(transformedUrl.isEmpty())
    }

    @Test
    fun initialUrlIsNonBlankButHasEmptyCharsInTheSides_getTransformedUrl_finalUrlIsTheExpectedOne() {
        val initialUrl = " /testPath.jpg "

        val transformedUrl = SUT.getTransformedUrl(initialUrl)

        val expectedUrl = "http://image.tmdb.org/t/p/w500/testPath.jpg"
        assertTrue(transformedUrl == expectedUrl)
    }

    @Test
    fun initialUrlIsNull_getTransformedUrl_finalUrlIsEmpty() {
        val initialUrl = null

        val transformedUrl = SUT.getTransformedUrl(initialUrl)

        assertTrue(transformedUrl.isEmpty())
    }
}