package com.haris.houlis.moviemaze.data.mapper

import com.haris.houlis.moviemaze.api.model.response.MoviesResponse
import com.haris.houlis.moviemaze.data.vo.Movie

class FakePopularMoviesMapper: DataMapper<MoviesResponse, List<Movie>> {
    override fun toDomain(source: MoviesResponse): List<Movie> = emptyList()
}