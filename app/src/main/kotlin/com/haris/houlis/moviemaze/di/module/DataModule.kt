package com.haris.houlis.moviemaze.di.module

import androidx.paging.ExperimentalPagingApi
import com.haris.houlis.moviemaze.api.model.response.APIMovieDetailsResponsesWrapper
import com.haris.houlis.moviemaze.api.model.response.MoviesResponse
import com.haris.houlis.moviemaze.api.service.TmdbService
import com.haris.houlis.moviemaze.data.db.wrapper.DatabaseWrapper
import com.haris.houlis.moviemaze.data.mapper.DataMapper
import com.haris.houlis.moviemaze.data.mapper.MovieDetailsDataMapper
import com.haris.houlis.moviemaze.data.mapper.MovieMapper
import com.haris.houlis.moviemaze.data.repository.DefaultMoviesRepository
import com.haris.houlis.moviemaze.data.source.DataSourcesWrapper
import com.haris.houlis.moviemaze.data.source.remote.MovieDetailsRemoteDataSource
import com.haris.houlis.moviemaze.data.source.remote.paging.PagingDataSources
import com.haris.houlis.moviemaze.data.source.remote.paging.mediator.PopularMoviesRemoteMediator
import com.haris.houlis.moviemaze.data.source.remote.paging.pagingsource.SearchMoviesPagingSource
import com.haris.houlis.moviemaze.data.transformer.PosterUrlTransformer
import com.haris.houlis.moviemaze.data.vo.Movie
import com.haris.houlis.moviemaze.data.vo.MovieDetails
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val dataModule = module {
    single { provideMoviesRepository(get(), get()) }
}

private fun provideMoviesRepository(
    service: TmdbService,
    databaseWrapper: DatabaseWrapper
): DefaultMoviesRepository =
    DefaultMoviesRepository(databaseWrapper, getDataSourcesWrapper(service, databaseWrapper))

private fun getDataSourcesWrapper(
    service: TmdbService,
    databaseWrapper: DatabaseWrapper
): DataSourcesWrapper =
    DataSourcesWrapper(
        getPagingSources(service, databaseWrapper),
        getDetailsRemoteDataSource(service)
    )

private fun getPagingSources(
    service: TmdbService,
    databaseWrapper: DatabaseWrapper
): PagingDataSources =
    PagingDataSources(
        getPopularMovieRemoteMediator(service, databaseWrapper),
        getSearchMoviesSource(service)
    )

@OptIn(ExperimentalPagingApi::class)
private fun getPopularMovieRemoteMediator(
    service: TmdbService,
    databaseWrapper: DatabaseWrapper
): PopularMoviesRemoteMediator =
    PopularMoviesRemoteMediator(service, databaseWrapper, getSearchMoviesDataMapper())

private fun getSearchMoviesSource(
    service: TmdbService
): SearchMoviesPagingSource = SearchMoviesPagingSource(service, getSearchMoviesDataMapper())

private fun getSearchMoviesDataMapper(): DataMapper<MoviesResponse, List<Movie>> =
    MovieMapper(PosterUrlTransformer())

private fun getDetailsRemoteDataSource(
    service: TmdbService
): MovieDetailsRemoteDataSource =
    MovieDetailsRemoteDataSource(service, getMovieDetailsDataMapper(), Dispatchers.Default)

private fun getMovieDetailsDataMapper(): DataMapper<APIMovieDetailsResponsesWrapper, MovieDetails> =
    MovieDetailsDataMapper(PosterUrlTransformer())


