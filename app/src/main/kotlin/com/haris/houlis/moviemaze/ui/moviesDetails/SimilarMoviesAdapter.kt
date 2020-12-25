package com.haris.houlis.moviemaze.ui.moviesDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.haris.houlis.moviemaze.R
import com.haris.houlis.moviemaze.databinding.ListItemSimilarMovieBinding

class SimilarMoviesAdapter : RecyclerView.Adapter<SimilarMoviesAdapter.SimilarMovieViewHolder>() {
    private var posters: List<String> = emptyList()

    fun setPosters(posters: List<String>) {
        this.posters = posters
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarMovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = ListItemSimilarMovieBinding.inflate(inflater, parent, false)
        return SimilarMovieViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = posters.size

    override fun onBindViewHolder(holder: SimilarMovieViewHolder, position: Int) =
        holder.bind(posters[position])

    class SimilarMovieViewHolder(private val binding: ListItemSimilarMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(posterUrl: String) {
            with(binding) {
                Glide.with(posterImageView)
                    .load(posterUrl)
                    .dontAnimate()
                    .placeholder(R.drawable.movie_placeholder)
                    .error(R.drawable.movie_placeholder)
                    .into(posterImageView)
            }
        }

    }

}