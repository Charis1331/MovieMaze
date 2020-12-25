package com.haris.houlis.moviemaze.data.vo

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.haris.houlis.moviemaze.R
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "movies")
@Parcelize
data class Movie(
    @PrimaryKey
    val id: Int,
    val posterUrl: String,
    val title: String,
    val releaseDate: ReleaseDate,
    val rating: MovieRating,
    var favorite: FavoriteAvailability,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
) : Parcelable {

    companion object {
        @DrawableRes
        fun FavoriteAvailability.Available.drawableResId(): Int =
            if (isFavorite) {
                R.drawable.ic_baseline_favorite
            } else {
                R.drawable.ic_baseline_favorite_border
            }
    }
}


sealed class MovieRating: Parcelable{
    @Parcelize
    object Empty: MovieRating()
    @Parcelize
    class Available(val value: Float): MovieRating()
}

sealed class FavoriteAvailability: Parcelable{
    @Parcelize
    object Unavailable: FavoriteAvailability()
    @Parcelize
    class Available(val isFavorite: Boolean): FavoriteAvailability()
}

sealed class ReleaseDate: Parcelable{
    @Parcelize
    object Unavailable: ReleaseDate()
    @Parcelize
    class Available(val value: String): ReleaseDate()
}