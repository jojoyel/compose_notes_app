package com.jojo.compose_notes_app.util

import androidx.annotation.StringRes
import com.jojo.compose_notes_app.R

enum class SelectableColors(val color: Long, @StringRes val displayName: Int) {
    NONE(0xff000000, R.string.color_none),
    RED(0xffF54C23, R.string.color_red),
    BLUE(0xff4487F5, R.string.color_blue),
    GREEN(0xffB2F55A, R.string.color_green);

    companion object {
        fun getColorFromHex(hex: Long?): SelectableColors {
            return values().find { it.color == hex } ?: NONE
        }
    }
}