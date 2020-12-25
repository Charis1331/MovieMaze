package com.haris.houlis.moviemaze.data.mapper

import com.haris.houlis.moviemaze.api.model.response.APIMovie
import com.haris.houlis.moviemaze.api.model.response.MoviesResponse
import com.haris.houlis.moviemaze.api.model.response.MoviesResponse.PopularMoviesResponse
import com.haris.houlis.moviemaze.api.model.response.MoviesResponse.SearchMoviesResponse
import com.haris.houlis.moviemaze.data.transformer.UrlTransformer
import com.haris.houlis.moviemaze.data.vo.FavoriteAvailability
import com.haris.houlis.moviemaze.data.vo.Movie
import com.haris.houlis.moviemaze.data.vo.MovieRating
import com.haris.houlis.moviemaze.data.vo.ReleaseDate
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

private const val DATE_FORMAT_PATTERN = "dd MMMM yyyy"

class MovieMapper(private val urlTransformer: UrlTransformer) :
    DataMapper<MoviesResponse, List<Movie>> {

    private val dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN, Locale.US)

    override fun toDomain(source: MoviesResponse): List<Movie> =
        source.results.map {
            val posterUrl = urlTransformer.getTransformedUrl(it.posterPath)
            val releaseDate = it.getReleaseDate()
            val rating = it.getConvertedRatingFromScale10To5()
            val favoriteSupport = getFavoriteSupport(source)
            Movie(
                it.id,
                posterUrl,
                it.title,
                releaseDate,
                rating,
                favoriteSupport
            )
        }

    private fun APIMovie.getConvertedRatingFromScale10To5(): MovieRating {
        return if (voteCount == 0) {
            MovieRating.Empty
        } else {
            val value = (rating * 5 / 10).toFloat()
            MovieRating.Available(value)
        }
    }

    private fun getFavoriteSupport(source: MoviesResponse): FavoriteAvailability =
        when (source) {
            is PopularMoviesResponse -> FavoriteAvailability.Available(false)
            is SearchMoviesResponse -> FavoriteAvailability.Unavailable
        }

    private fun APIMovie.getReleaseDate(): ReleaseDate =
        if (!releaseDate.isNullOrBlank()) {
            ReleaseDate.Available(getFormattedDate())
        } else {
            ReleaseDate.Unavailable
        }

    private fun APIMovie.getFormattedDate(): String =
        LocalDate.parse(releaseDate)
            .format(dateFormatter)
}