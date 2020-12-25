package com.haris.houlis.moviemaze.extensions

import android.view.View
import android.view.ViewGroup
import androidx.core.view.*
import androidx.core.view.WindowInsetsCompat.Type.ime

fun View.visible() {
    isVisible = true
}

fun View.gone() {
    isGone = true
}

fun View.hideImeAndClearFocus() {
    ViewCompat.getWindowInsetsController(this)
        ?.hide(ime())
    clearFocus()
}

fun View.fitToSystemWindows(viewToInset: View) {
    setOnApplyWindowInsetsListener { _, insets ->
        viewToInset.updateMargins(top = insets.systemWindowInsetTop)
        insets
    }
}

fun View.updateMargins(
    top: Int = marginTop,
    bottom: Int = marginBottom,
    start: Int = marginStart,
    end: Int = marginEnd
) {
    val oldLp = layoutParams as? ViewGroup.MarginLayoutParams ?: return
    oldLp.let {
        it.topMargin = top
        it.bottomMargin = bottom
        it.marginStart = start
        it.marginEnd = end
    }
}
