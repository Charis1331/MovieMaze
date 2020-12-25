package com.haris.houlis.moviemaze.data.source

import com.haris.houlis.moviemaze.data.source.remote.MovieDetailsRemoteDataSource
import com.haris.houlis.moviemaze.data.source.remote.paging.PagingDataSources

class DataSourcesWrapper(
    val pagingSources: PagingDataSources,
    val movieDetailsSource: MovieDetailsRemoteDataSource
)