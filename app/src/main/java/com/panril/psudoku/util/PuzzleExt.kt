package com.panril.psudoku.util

import sudoku.puzzle.Puzzle
import sudoku.puzzle.PuzzleReader

fun puzzleToString(puzzle: Puzzle): String {
    val state = puzzle.startState
    return state.joinToString("\n") { it -> it.joinToString("_")}
}

fun puzzleFromString(str: String): Puzzle {
    val nums = str.split("\n").map {it -> it.split("_").map { s -> s.toInt() }}
    return PuzzleReader.makePuzzle(nums as List<List<Int>>, 3, 3)
}