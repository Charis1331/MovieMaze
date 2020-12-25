package com.haris.houlis.moviemaze.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.haris.houlis.moviemaze.data.vo.FavoriteAvailability
import com.haris.houlis.moviemaze.data.vo.Movie

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movies: List<Movie>)

    @Query("SELECT * FROM movies ORDER BY created_at ASC")
    fun allMovies(): PagingSource<Int, Movie>

    @Query("UPDATE movies SET favorite = :favoriteAvailability WHERE id = :id")
    suspend fun updateFavorite(id: Int, favoriteAvailability: FavoriteAvailability): Int

    @Query("DELETE FROM movies")
    suspend fun clearMovies()
}