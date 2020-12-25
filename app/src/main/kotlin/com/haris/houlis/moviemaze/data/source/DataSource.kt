package com.haris.houlis.moviemaze.data.source

import com.haris.houlis.moviemaze.data.source.remote.UNKNOWN_ERROR_CODE
import com.haris.houlis.moviemaze.data.vo.MovieDetails
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException

interface DataSource {

    suspend fun getMovieDetails(id: Int): ResultWrapper<MovieDetails>

    suspend fun <T> safeApiCall(
        dispatcher: CoroutineDispatcher,
        apiCall: suspend () -> T
    ): ResultWrapper<T> {
        return withContext(dispatcher) {
            try {
                ResultWrapper.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is IOException -> ResultWrapper.NetworkError
                    is HttpException -> {
                        val code = throwable.code()
                        ResultWrapper.Error(code)
                    }
                    else -> {
                        ResultWrapper.Error()
                    }
                }
            }
        }
    }

    sealed class ResultWrapper<out R> {
        data class Success<out D>(val data: D?) : ResultWrapper<D>()
        data class Error(val code: Int = UNKNOWN_ERROR_CODE) : ResultWrapper<Nothing>()
        object NetworkError : ResultWrapper<Nothing>()
    }
}