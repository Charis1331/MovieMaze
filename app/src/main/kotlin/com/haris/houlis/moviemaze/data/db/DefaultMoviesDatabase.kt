package com.haris.houlis.moviemaze.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.haris.houlis.moviemaze.data.db.dao.MoviesDao
import com.haris.houlis.moviemaze.data.db.dao.RemoteKeysDao
import com.haris.houlis.moviemaze.data.db.typeconverter.MovieTypeConverter
import com.haris.houlis.moviemaze.data.vo.Movie
import com.haris.houlis.moviemaze.data.vo.RemoteKeys

@Database(entities = [Movie::class, RemoteKeys::class], version = 1, exportSchema = false)
@TypeConverters(MovieTypeConverter::class)
abstract class DefaultMoviesDatabase : RoomDatabase() {
    abstract fun moviesDao(): MoviesDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}