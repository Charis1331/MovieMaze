package com.haris.houlis.moviemaze.util

import android.app.Activity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat.Type.ime

internal fun Activity.isImeVisible() =
    ViewCompat.getRootWindowInsets(window.decorView)!!
        .isVisible(ime())