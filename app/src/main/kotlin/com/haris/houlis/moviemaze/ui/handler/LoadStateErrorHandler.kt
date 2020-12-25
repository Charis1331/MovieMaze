package com.haris.houlis.moviemaze.ui.handler

import android.content.Context
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.haris.houlis.moviemaze.R
import com.haris.houlis.moviemaze.data.source.remote.UNKNOWN_ERROR_CODE

class LoadStateErrorHandler(private val context: Context) {

    fun getLoadStateErrorThrowable(loadState: CombinedLoadStates): SafeThrowable? {
        val errorClassType = LoadState.Error::class.java
        val stateError = with(loadState) {
            source.append asOf errorClassType
                ?: source.prepend asOf errorClassType
                ?: source.refresh asOf errorClassType
                ?: append asOf errorClassType
                ?: prepend asOf errorClassType
                ?: refresh asOf errorClassType
                ?: mediator?.append asOf errorClassType
                ?: mediator?.prepend asOf errorClassType
                ?: mediator?.refresh asOf errorClassType
        }
        if (stateError != null) {
            val errorMessage: String? = stateError.error.message
            return if (errorMessage.isNullOrEmpty()) {
                SafeThrowable(context.getString(R.string.generic_error, UNKNOWN_ERROR_CODE))
            } else {
                SafeThrowable(errorMessage)
            }
        }
        return null
    }

    private inline infix fun <reified T> Any?.asOf(value: Class<T>): T? {
        return if (this != null && value.isInstance(this)) {
            this as T
        } else {
            null
        }
    }
}

class SafeThrowable(val message: String)