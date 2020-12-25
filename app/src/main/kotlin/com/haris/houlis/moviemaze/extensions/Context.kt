package com.haris.houlis.moviemaze.extensions

import android.app.Activity
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.haris.houlis.moviemaze.R

fun Fragment.isPortrait(): Boolean = resources.getBoolean(R.bool.is_portrait)

fun Activity.statusBarColor(@ColorRes colorResId: Int) {
    window.statusBarColor = ContextCompat.getColor(this, colorResId)
}