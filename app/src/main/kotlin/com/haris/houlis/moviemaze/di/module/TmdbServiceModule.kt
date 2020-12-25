package com.haris.houlis.moviemaze.di.module

import com.haris.houlis.moviemaze.api.service.TmdbService
import com.haris.houlis.moviemaze.api.service.interceptor.TokenInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.themoviedb.org/"

val serviceModule = module {
    single { provideApiService() }
}

private fun provideApiService(): TmdbService =
     Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(getClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TmdbService::class.java)

private fun getClient(): OkHttpClient {
    val logger = HttpLoggingInterceptor().apply {
        level = Level.BODY
    }
    return OkHttpClient.Builder()
        .addInterceptor(logger)
        .addInterceptor(TokenInterceptor)
        .build()
}