package com.haris.houlis.moviemaze.api.service.interceptor

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Interceptor.*
import okhttp3.Request
import okhttp3.Response

object TokenInterceptor : Interceptor {

    private const val API_KEY = "YOUR_API_KEY"
    private const val PARAMETER_NAME_API_KEY = "api_key"

    override fun intercept(chain: Chain): Response =
        with(chain) {
            val newUrl = newUrlWithQuery(PARAMETER_NAME_API_KEY, API_KEY)
            val newRequest = requestWithNewUrl(newUrl)
            proceed(newRequest)
        }

    private fun Chain.newUrlWithQuery(key: String, value: String): HttpUrl =
        requestUrlNewBuilder().apply {
            addQueryParameter(key, value)
        }
            .build()

    private fun Chain.requestUrlNewBuilder(): HttpUrl.Builder =
        request()
            .url
            .newBuilder()

    private fun Chain.requestWithNewUrl(newUrl: HttpUrl): Request =
        request()
            .newBuilder()
            .url(newUrl)
            .build()
}