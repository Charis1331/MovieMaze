package com.haris.houlis.moviemaze.data.transformer

import android.net.Uri
import com.haris.houlis.moviemaze.extensions.isBlankOrHasWhiteSpaces
import kotlin.contracts.ExperimentalContracts

interface UrlTransformer {
    fun getTransformedUrl(initialUrl: String?): String
}

@OptIn(ExperimentalContracts::class)
class PosterUrlTransformer : UrlTransformer {

    override fun getTransformedUrl(initialUrl: String?): String {
        val trimmedInitialUrl = initialUrl?.trim()
        if (trimmedInitialUrl.isBlankOrHasWhiteSpaces()) return EMPTY_CHAR

        val imageUrl = trimmedInitialUrl.buildPosterUrl()
        return if (imageUrl.isBlank()) {
            EMPTY_CHAR
        } else {
            imageUrl
        }
    }


    private fun String.buildPosterUrl(imageSize: String = BIG_POSTER_SIZE) =
        Uri.parse(POSTER_BASE_URL)
            .buildUpon()
            .appendEncodedPath(imageSize + this)
            .build()
            .toString()

    companion object {
        const val POSTER_BASE_URL = "http://image.tmdb.org/t/p/"
        const val BIG_POSTER_SIZE = "w500"

        const val EMPTY_CHAR = ""
    }
}