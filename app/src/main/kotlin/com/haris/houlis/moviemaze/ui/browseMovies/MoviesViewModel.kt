package com.haris.houlis.moviemaze.ui.browseMovies

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.haris.houlis.moviemaze.data.repository.DefaultMoviesRepository
import com.haris.houlis.moviemaze.data.vo.Movie
import com.haris.houlis.moviemaze.ui.browseMovies.MoviesViewModel.SearchMovieQuery.Initial
import com.haris.houlis.moviemaze.ui.browseMovies.MoviesViewModel.SearchMovieQuery.Valid
import com.haris.houlis.moviemaze.ui.manager.SharedPreferencesManager
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class MoviesViewModel(
    private val preferencesManager: SharedPreferencesManager,
    private val repository: DefaultMoviesRepository
) : ViewModel() {

    private val _queryFlow = MutableStateFlow(readQueryFromPreferences())
    val queryFlow: StateFlow<SearchMovieQuery> = _queryFlow

    val pagingDataFlow = _queryFlow.flatMapLatest { query ->
        val result =
            if (query is Valid) {
                repository.searchMovies(query.value)
            } else {
                repository.getPopularMovies()
            }
        result.cachedIn(viewModelScope)
    }

    fun getPopularMovies() {
        val newValue = Initial
        writeQueryToPreferences(newValue)
        _queryFlow.value = newValue
    }

    fun searchMovies(movieTitle: String) {
        if (!shouldSearchForMovies(movieTitle)) return

        val newValue = Valid(movieTitle)
        writeQueryToPreferences(newValue)
        _queryFlow.value = newValue
    }

    fun updateFavorite(movie: Movie) {
        viewModelScope.launch {
            repository.updateFavorite(movie)
        }
    }

    private fun shouldSearchForMovies(movieTitle: String): Boolean {
        val currentValue = readQueryFromPreferences()
        return currentValue is Initial ||
                (currentValue is Valid && currentValue.value != movieTitle)
    }

    private fun readQueryFromPreferences(): SearchMovieQuery =
        preferencesManager.read<Valid>(KEY_QUERY) ?: Initial

    private fun writeQueryToPreferences(newValue: SearchMovieQuery) =
        preferencesManager.write(KEY_QUERY, newValue)


     companion object {
        const val KEY_QUERY = "query_key"
    }

    sealed class SearchMovieQuery : Parcelable {
        @Parcelize
        object Initial : SearchMovieQuery()

        @Parcelize
        class Valid(val value: String) : SearchMovieQuery()
    }

}