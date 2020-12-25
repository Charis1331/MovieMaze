package com.haris.houlis.moviemaze.extensions

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

private const val WHITE_SPACE_CHAR = " "

@ExperimentalContracts
fun String?.isBlankOrHasWhiteSpaces(): Boolean {
    contract {
        returns(false) implies (this@isBlankOrHasWhiteSpaces != null)
    }

    return isNullOrBlank() ||
            this?.contains(WHITE_SPACE_CHAR) ?: true
}
