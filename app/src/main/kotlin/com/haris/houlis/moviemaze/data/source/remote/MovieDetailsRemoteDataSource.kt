package com.haris.houlis.moviemaze.data.source.remote

import com.haris.houlis.moviemaze.api.model.response.APIMovieDetailsResponsesWrapper
import com.haris.houlis.moviemaze.api.model.response.MovieBasicDetailsResponse
import com.haris.houlis.moviemaze.api.model.response.MovieStakeHoldersResponse
import com.haris.houlis.moviemaze.api.service.TmdbService
import com.haris.houlis.moviemaze.data.mapper.DataMapper
import com.haris.houlis.moviemaze.data.source.DataSource
import com.haris.houlis.moviemaze.data.source.DataSource.*
import com.haris.houlis.moviemaze.data.source.DataSource.ResultWrapper.Error
import com.haris.houlis.moviemaze.data.source.DataSource.ResultWrapper.Success
import com.haris.houlis.moviemaze.data.vo.MovieDetails
import kotlinx.coroutines.*

const val UNKNOWN_ERROR_CODE = -1

class MovieDetailsRemoteDataSource(
    private val service: TmdbService,
    private val dataMapper: DataMapper<APIMovieDetailsResponsesWrapper, MovieDetails>,
    private val dispatcher: CoroutineDispatcher
) : DataSource {

    override suspend fun getMovieDetails(id: Int): ResultWrapper<MovieDetails> =
        coroutineScope {
            val basicDetailsCall = getCallAsync { service.getMovieBasicDetails(id) }
            val stakeHoldersCall = getCallAsync { service.getMovieStakeHolders(id) }
            val reviewsCall = getCallAsync { service.getMovieReviews(id) }
            val similarMoviesCall = getCallAsync { service.getSimilarMovies(id) }

            val basicDetailsResult = basicDetailsCall.await()
            val stakeHoldersResult = stakeHoldersCall.await()
            val reviewsResult = reviewsCall.await()
            val similarMoviesResult = similarMoviesCall.await()

            if (basicDetailsResult is Success && stakeHoldersResult is Success &&
                basicDetailsResult.data != null && stakeHoldersResult.data != null
            ) {
                val wrapper = APIMovieDetailsResponsesWrapper(
                    basicDetailsResult.data,
                    stakeHoldersResult.data,
                    reviewsResult,
                    similarMoviesResult
                )
                Success(dataMapper.toDomain(wrapper))
            } else {
                getError(basicDetailsResult, stakeHoldersResult)
            }
        }

    private fun getError(
        basicDetailsResult: ResultWrapper<MovieBasicDetailsResponse>,
        stakeHoldersResult: ResultWrapper<MovieStakeHoldersResponse>
    ): Error = when {
        basicDetailsResult is Error -> Error(basicDetailsResult.code)
        stakeHoldersResult is Error -> Error(stakeHoldersResult.code)
        else -> Error()
    }

    private fun <T> CoroutineScope.getCallAsync(block: suspend () -> T): Deferred<ResultWrapper<T>> =
        async {
            safeApiCall(dispatcher) {
                block.invoke()
            }
        }
}