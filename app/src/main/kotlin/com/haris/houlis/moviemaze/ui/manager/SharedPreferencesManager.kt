package com.haris.houlis.moviemaze.ui.manager

import android.content.SharedPreferences
import com.google.gson.Gson
import com.haris.houlis.moviemaze.extensions.deserializeOrNull

class SharedPreferencesManager(private val preferences: SharedPreferences) {

    private val gson = Gson()

    fun write(key: String, value: Any) {
        val serializedValue = gson.toJson(value)
        with(preferences.edit()) {
            putString(key, serializedValue)
            apply()
        }
    }

    internal inline fun <reified T> read(key: String): T? {
        val serializedValue = preferences.getString(key, null)
        return gson.deserializeOrNull<T>(serializedValue)
    }

}