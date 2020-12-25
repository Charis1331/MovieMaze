package com.haris.houlis.moviemaze.data.transformer

import com.haris.houlis.moviemaze.data.transformer.PosterUrlTransformer.Companion.EMPTY_CHAR

class FakeUrlTransformer : UrlTransformer {
    override fun getTransformedUrl(initialUrl: String?): String = EMPTY_CHAR
}