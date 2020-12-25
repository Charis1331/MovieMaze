package com.haris.houlis.moviemaze.data.vo


class MovieDetails(
    private val genres: List<String>,
    val overview: String,
    private val cast: List<String>,
    val director: Director,
    val reviews: MovieReviews,
    val similarMovies: SimilarMovies
) {
    fun getGenres(): String = genres.getListOfStringsAsOneString()

    fun getCast(): String = cast.getListOfStringsAsOneString()

    private fun List<String>.getListOfStringsAsOneString(): String {
        val stringBuilder = StringBuilder()
        forEachIndexed { index, item ->
            stringBuilder.append(item)
            if (index != size - 1) {
                stringBuilder.append(", ")
            }
        }
        return stringBuilder.toString()
    }
}

class MovieReview(val author: String, val content: String)

sealed class Director {
    object Empty : Director()
    class Available(val name: String) : Director()
}

sealed class MovieReviews {
    object Unavailable : MovieReviews()
    class Available(val reviews: List<MovieReview>) : MovieReviews()
}

sealed class SimilarMovies {
    object Unavailable : SimilarMovies()
    class Available(val posters: List<String>) : SimilarMovies()
}