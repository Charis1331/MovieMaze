package com.haris.houlis.moviemaze.extensions

import com.google.gson.Gson

const val EMPTY_VALUE_OBJECT = "{}"

inline fun <reified T> Gson.deserializeOrNull(serializedValue: String?): T? {
    val isSerializedValueNullOrEmpty =
        serializedValue == null || serializedValue == EMPTY_VALUE_OBJECT
    return if (isSerializedValueNullOrEmpty) {
        null
    } else {
        fromJson(serializedValue, T::class.java)
    }
}