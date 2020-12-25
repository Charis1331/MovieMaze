package com.haris.houlis.moviemaze.di.module

import android.content.Context
import androidx.room.Room
import com.haris.houlis.moviemaze.data.db.dao.RemoteKeysDao
import com.haris.houlis.moviemaze.data.db.dao.MoviesDao
import com.haris.houlis.moviemaze.data.db.DefaultMoviesDatabase
import com.haris.houlis.moviemaze.data.db.wrapper.DatabaseWrapper
import com.haris.houlis.moviemaze.data.db.wrapper.DefaultDatabaseWrapper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val movieDbModule = module {
    single { provideMovidesDb(androidContext()) }
}

val movieDaosModule = module {
    single { provideMoviesDao(get()) }
    single { provideRemoteKeysDao(get()) }
    single { provideDatabaseWrapper(get()) }
}

private fun provideMovidesDb(context: Context): DefaultMoviesDatabase =
    Room.databaseBuilder(
        context.applicationContext,
        DefaultMoviesDatabase::class.java, "Movies.db"
    ).build()

private fun provideMoviesDao(moviesDb: DefaultMoviesDatabase): MoviesDao = moviesDb.moviesDao()

private fun provideRemoteKeysDao(moviesDb: DefaultMoviesDatabase): RemoteKeysDao =
    moviesDb.remoteKeysDao()

private fun provideDatabaseWrapper(moviesDb: DefaultMoviesDatabase): DatabaseWrapper =
    DefaultDatabaseWrapper(moviesDb)
