package com.haris.houlis.moviemaze.ui.browseMovies

import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.haris.houlis.moviemaze.data.vo.Movie

class MoviesAdapter(private val listener: MovieClickListener) :
    PagingDataAdapter<Movie, MovieViewHolder>(MOVIE_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder =
        MovieViewHolder.create(parent, listener)

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) =
        holder.bind(getItem(position))

    interface MovieClickListener {
        fun onMovieClick(movie: Movie, posterImage: ImageView)
        fun onFavoriteClick(movie: Movie)
    }

    companion object {
        private val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem.id == newItem.id &&
                        oldItem.favorite == newItem.favorite

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem == newItem
        }

    }
}