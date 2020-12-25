package com.haris.houlis.moviemaze

import android.app.Application
import com.haris.houlis.moviemaze.di.module.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Suppress("unused")
open class MovieMaze : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MovieMaze)
            modules(
                listOf(movieDbModule, movieDaosModule, dataModule, fragmentsModule, serviceModule)
            )
        }
    }

}