package com.haris.houlis.moviemaze.di.module

import android.content.Context
import com.haris.houlis.moviemaze.ui.handler.LoadStateErrorHandler
import com.haris.houlis.moviemaze.ui.MainActivity
import com.haris.houlis.moviemaze.ui.manager.SharedPreferencesManager
import com.haris.houlis.moviemaze.ui.moviesDetails.MovieDetailsViewModel
import com.haris.houlis.moviemaze.ui.browseMovies.MoviesViewModel
import com.haris.houlis.moviemaze.ui.browseMovies.MoviesFragment
import com.haris.houlis.moviemaze.ui.moviesDetails.MovieDetailsFragment
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val fragmentsModule = module {
    scope<MoviesFragment> {
        scoped { LoadStateErrorHandler(androidContext()) }
        viewModel { (activity: MainActivity) ->
            MoviesViewModel(
                provideSharedPreferencesManager(
                    activity
                ), get()
            )
        }
    }

    scope<MovieDetailsFragment> {
        viewModel { MovieDetailsViewModel(get()) }
    }
}

private fun provideSharedPreferencesManager(activity: MainActivity): SharedPreferencesManager =
    SharedPreferencesManager(activity.getPreferences(Context.MODE_PRIVATE))