package com.haris.houlis.moviemaze.ui.moviesDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haris.houlis.moviemaze.data.repository.DefaultMoviesRepository
import com.haris.houlis.moviemaze.data.source.DataSource.ResultWrapper.*
import com.haris.houlis.moviemaze.data.vo.FavoriteAvailability
import com.haris.houlis.moviemaze.data.vo.FavoriteAvailability.Available
import com.haris.houlis.moviemaze.data.vo.FavoriteAvailability.Unavailable
import com.haris.houlis.moviemaze.data.vo.Movie
import com.haris.houlis.moviemaze.data.vo.MovieDetails
import kotlinx.coroutines.launch

class MovieDetailsViewModel constructor(private val repository: DefaultMoviesRepository) : ViewModel() {

    private val _details = MutableLiveData<MovieDetails?>()
    val details: LiveData<MovieDetails?> = _details

    private val _favoriteStatusUpdated = MutableLiveData<FavoriteAvailability>()
    val favoriteStatusUpdated = _favoriteStatusUpdated

    private val _genericError = MutableLiveData<Int>()
    val genericError = _genericError

    private val _networkError = MutableLiveData<Boolean>()
    val networkError = _networkError

    fun fetchDetails(movie: Movie) {
        viewModelScope.launch {
            when (val result = repository.getMovieDetails(movie)) {
                is Success -> {
                    _details.value = result.data
                    _networkError.value = false
                }
                is Error -> _genericError.value = result.code
                NetworkError -> _networkError.value = true
            }
        }
    }

    fun updateFavorite(movie: Movie) {
        val currentFavoriteAvailability = movie.favorite
        if (currentFavoriteAvailability == Unavailable) {
            _favoriteStatusUpdated.value = currentFavoriteAvailability
            return
        }
        updateFavoriteStatusInDb(movie)
    }

    private fun updateFavoriteStatusInDb(movie: Movie) {
        viewModelScope.launch {
            val rowsUpdated = repository.updateFavorite(movie)
            _favoriteStatusUpdated.value = getNewFavoriteStatusValue(movie, rowsUpdated)
        }
    }

    private fun getNewFavoriteStatusValue(movie: Movie, rowsUpdated: Int): FavoriteAvailability {
        val currentFavoriteAvailability = movie.favorite
        return if (rowsUpdated == 1 && currentFavoriteAvailability is Available) {
            Available(!currentFavoriteAvailability.isFavorite)
        } else {
            currentFavoriteAvailability
        }
    }
}