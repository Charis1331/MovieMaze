package com.haris.houlis.moviemaze.data.db.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.haris.houlis.moviemaze.data.vo.FavoriteAvailability
import com.haris.houlis.moviemaze.data.vo.MovieRating
import com.haris.houlis.moviemaze.data.vo.ReleaseDate
import com.haris.houlis.moviemaze.extensions.deserializeOrNull

class MovieTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun stringToRating(s: String): MovieRating =
        gson.deserializeOrNull<MovieRating.Available>(s) ?: MovieRating.Empty

    @TypeConverter
    fun ratingToString(movieRating: MovieRating): String = gson.toJson(movieRating)

    @TypeConverter
    fun stringToFavoriteSupport(s: String): FavoriteAvailability =
        gson.deserializeOrNull<FavoriteAvailability.Available>(s)
            ?: FavoriteAvailability.Unavailable

    @TypeConverter
    fun favoriteSupportToString(favoriteAvailability: FavoriteAvailability): String =
        gson.toJson(favoriteAvailability)

    @TypeConverter
    fun stringToReleaseDate(s: String): ReleaseDate =
        gson.deserializeOrNull<ReleaseDate.Available>(s) ?: ReleaseDate.Unavailable

    @TypeConverter
    fun releaseDateToString(releaseDate: ReleaseDate): String = gson.toJson(releaseDate)
}