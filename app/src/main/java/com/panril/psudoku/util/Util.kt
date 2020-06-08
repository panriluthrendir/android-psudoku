package com.panril.psudoku.util

import android.content.Context
import android.os.SystemClock
import android.widget.Chronometer
import androidx.preference.PreferenceManager
import com.panril.psudoku.R
import sudoku.puzzle.Puzzle
import sudoku.puzzle.PuzzleGenerator


fun buttonTagFromPosition(row: Int, col: Int): String {
    return "bt_${row}_${col}"
}

fun keyboardTagFromIndex(index: Int): String {
    return "kb_$index"
}

fun keyboardIndexFromTag(tag: String): Int {
    return tag.split("_")[1].toInt()
}

fun generatePuzzleFromPreferences(context: Context): Puzzle {
    val prefs =
        PreferenceManager
            .getDefaultSharedPreferences(context)
    val difficulty = prefs.getString("difficulty", "Easy")
    val size = prefs.getString("size", "3x3")
    var puzzleHeight = 3
    var puzzleWidth = 3
    if (size != null) {
        puzzleHeight = size.split("x")[0].toInt()
        puzzleWidth = size.split("x")[1].toInt()
    }
    val minFill = when (difficulty) {
        "Easy" -> context.resources.getInteger(R.integer.minfill_easy)
        "Medium" -> context.resources.getInteger(R.integer.minfill_medium)
        "Hard" -> context.resources.getInteger(R.integer.minfill_hard)
        else -> context.resources.getInteger(R.integer.minfill_easy)
    }
    val shuffleCount = context.resources.getInteger(R.integer.shuffle_count)
    val swapCount = context.resources.getInteger(R.integer.swap_count)
    val generator = PuzzleGenerator(shuffleCount, swapCount, minFill)
    return generator.randomPuzzle(puzzleHeight, puzzleWidth)
}
/*
fun sudokuBackgroundId(context: Context, height: Int, width: Int): Int {
    val name = "sudoku_background_${height}_${width}"
    return context.resources.getIdentifier(name, "drawable", context.packageName)
}
*/

fun backgroundId(size: String): Int = when(size) {
    "2x2" -> R.drawable.sudoku_background_2x2
    "3x2" -> R.drawable.sudoku_background_3x2
    "3x3" -> R.drawable.sudoku_background_3x3
    "4x3" -> R.drawable.sudoku_background_4x3
    else -> 0
}

fun numToText(num: Int) = when(num) {
    10 -> "A"
    11 -> "B"
    12 -> "C"
    else -> num.toString()
}

fun chronoTime(chrono: Chronometer): Long {
    return SystemClock.elapsedRealtime() - chrono.base
}

fun millisToTimeStr(millis: Long): String {
    val minutes = millis / 60000
    val seconds = (millis % 60000) / 1000
    return "${minutes}: ${seconds}"
}
