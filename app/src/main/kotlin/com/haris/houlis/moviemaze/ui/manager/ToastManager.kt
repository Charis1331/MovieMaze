package com.haris.houlis.moviemaze.ui.manager

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast

object ToastManager {

    private lateinit var toast: Toast

    @SuppressLint("ShowToast")
    fun showToast(context: Context, message: String) {
        if (!ToastManager::toast.isInitialized) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        } else {
            toast.setText(message)
        }
        toast.show()
    }

}