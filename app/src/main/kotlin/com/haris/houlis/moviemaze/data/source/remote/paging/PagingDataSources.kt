package com.haris.houlis.moviemaze.data.source.remote.paging

import com.haris.houlis.moviemaze.data.source.remote.paging.mediator.PopularMoviesRemoteMediator
import com.haris.houlis.moviemaze.data.source.remote.paging.pagingsource.SearchMoviesPagingSource

class PagingDataSources(
    val popularMoviesRemoteMediator: PopularMoviesRemoteMediator,
    val searchMoviesPagingSource: SearchMoviesPagingSource
)