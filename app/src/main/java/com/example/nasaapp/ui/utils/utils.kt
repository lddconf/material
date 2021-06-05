package com.example.nasaapp.ui.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.nasaapp.R
import com.example.nasaapp.model.data.NoteColor

fun NoteColor.toColorResId(): Int = when (this) {
    NoteColor.WHITE -> R.color.note_white
    NoteColor.RED -> R.color.red
    NoteColor.ORANGE -> R.color.orange
    NoteColor.YELLOW -> R.color.yellow
    NoteColor.GREEN -> R.color.green
    NoteColor.BLUE -> R.color.blue
    NoteColor.DARK_BLUE -> R.color.dark_blue
    NoteColor.VIOLET -> R.color.violet
}

fun NoteColor.toColor(context: Context): Int =
    ContextCompat.getColor(context, this.toColorResId())

fun colorToPredefinedColor(
    context: Context,
    color: Int,
    defaultColor: NoteColor
): NoteColor = when (color) {
    NoteColor.WHITE.toColor(context) -> NoteColor.WHITE
    NoteColor.RED.toColor(context) -> NoteColor.RED
    NoteColor.ORANGE.toColor(context) -> NoteColor.ORANGE
    NoteColor.YELLOW.toColor(context) -> NoteColor.YELLOW
    NoteColor.GREEN.toColor(context) -> NoteColor.GREEN
    NoteColor.BLUE.toColor(context) -> NoteColor.BLUE
    NoteColor.DARK_BLUE.toColor(context) -> NoteColor.DARK_BLUE
    NoteColor.VIOLET.toColor(context) -> NoteColor.VIOLET
    else -> defaultColor
}

fun Context.dip(value: Int) = (value* resources.displayMetrics.density).toInt()