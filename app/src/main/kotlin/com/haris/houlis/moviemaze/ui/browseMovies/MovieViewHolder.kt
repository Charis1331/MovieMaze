package com.haris.houlis.moviemaze.ui.browseMovies

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.haris.houlis.moviemaze.R
import com.haris.houlis.moviemaze.data.vo.FavoriteAvailability
import com.haris.houlis.moviemaze.data.vo.Movie
import com.haris.houlis.moviemaze.data.vo.Movie.Companion.drawableResId
import com.haris.houlis.moviemaze.data.vo.MovieRating
import com.haris.houlis.moviemaze.databinding.ListItemMovieBinding
import com.haris.houlis.moviemaze.extensions.gone
import com.haris.houlis.moviemaze.extensions.visible
import com.haris.houlis.moviemaze.ui.browseMovies.MoviesAdapter.MovieClickListener

class MovieViewHolder(
    private val binding: ListItemMovieBinding,
    private val listener: MovieClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: Movie?) {
        with(binding) {
            if (movie != null) {
                bindUiOnValidMovieData(movie)
            } else {
                bindUiOnNonValidMovieData()
            }
        }
    }

    private fun ListItemMovieBinding.bindUiOnValidMovieData(movie: Movie) {
        root.setOnClickListener { listener.onMovieClick(movie, posterImageView) }

        titleTextView.text = movie.title

        Glide.with(posterImageView)
            .load(movie.posterUrl)
            .dontAnimate()
            .placeholder(R.drawable.movie_placeholder)
            .error(R.drawable.movie_placeholder)
            .into(posterImageView)

        posterImageView.setTransitionNameForValidMovie(movie)

        bindFavoriteImageViewOnValidMovie(movie)

        bindRatingUiOnValidMovie(movie)
    }

    private fun ListItemMovieBinding.bindFavoriteImageViewOnValidMovie(movie: Movie) {
        with(favoriteImageView) {
            setVisibilityAndResource(movie)
            setOnClickListener { listener.onFavoriteClick(movie) }
        }
    }

    private fun ImageView.setTransitionNameForValidMovie(movie: Movie) {
        val transitionNamePrefix = context.getString(R.string.transition_name_prefix)
        val finalTransitionName = transitionNamePrefix + movie.id.toString()
        transitionName = finalTransitionName
    }

    private fun ImageView.setVisibilityAndResource(movie: Movie) =
        when (val favoriteSupport = movie.favorite) {
            FavoriteAvailability.Unavailable -> gone()
            is FavoriteAvailability.Available -> {
                setImageResource(favoriteSupport.drawableResId())
                visible()
            }
        }

    private fun ListItemMovieBinding.bindRatingUiOnValidMovie(movie: Movie) =
        when (val movieRating = movie.rating) {
            MovieRating.Empty -> {
                ratingBar.gone()
                noRatingTextView.visible()
            }
            is MovieRating.Available -> {
                noRatingTextView.gone()
                with(ratingBar) {
                    rating = movieRating.value
                    visible()
                }
            }
        }

    private fun ListItemMovieBinding.bindUiOnNonValidMovieData() {
        root.setOnClickListener(null)

        posterImageView.transitionName = null

        titleTextView.setText(R.string.not_available)

        bindRatingUiOnNonValidMovie()

        bindFavoriteImageOnNonValidMovie()
    }

    private fun ListItemMovieBinding.bindRatingUiOnNonValidMovie() {
        ratingBar.gone()
        noRatingTextView.visible()
    }

    private fun ListItemMovieBinding.bindFavoriteImageOnNonValidMovie() {
        with(favoriteImageView) {
            setOnClickListener(null)
            gone()
        }
    }

    companion object {
        fun create(parent: ViewGroup, listener: MovieClickListener): MovieViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemBinding = ListItemMovieBinding.inflate(inflater, parent, false)
            return MovieViewHolder(itemBinding, listener)
        }
    }
}