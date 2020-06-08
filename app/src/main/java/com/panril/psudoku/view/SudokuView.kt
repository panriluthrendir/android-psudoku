package com.panril.psudoku.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.widget.Button
import android.widget.GridLayout
import androidx.lifecycle.ViewModelProviders
import com.panril.psudoku.R
import com.panril.psudoku.activity.SudokuActivity
import com.panril.psudoku.util.backgroundId
import com.panril.psudoku.util.buttonTagFromPosition
import com.panril.psudoku.util.numToText

import sudoku.core.Filler
import sudoku.core.Sudoku
import sudoku.puzzle.Puzzle
import sudoku.solver.LinearSolver


class SudokuView(context: Context, attrs: AttributeSet): GridLayout(context, attrs) {

    private lateinit var basePuzzle: Puzzle
    lateinit var sudoku: Sudoku

    init {
        resize(3, 3)
    }

    fun newPuzzle(puzzle: Puzzle) {
        val height = puzzle.height
        val width = puzzle.width
        if (height * width != rowCount) {
            resize(height, width)
        }
        basePuzzle = puzzle
        sudoku = Sudoku(basePuzzle)
        update(emptyList<Filler>())
    }

    fun resize(puzzleHeight: Int, puzzleWidth: Int) {
        removeAllViews()
        rowCount = puzzleHeight * puzzleWidth
        columnCount = puzzleHeight * puzzleWidth
        for (i in 0 until rowCount) {
            for (j in 0 until columnCount) {
                val themeContext = ContextThemeWrapper(context, R.style.SudokuButton)
                val button = Button(context)
                val params = LayoutParams(themeContext, null)
                button.layoutParams = params
                button.tag = buttonTagFromPosition(i, j)
                button.setOnClickListener { _ -> makeFill(i, j) }
                button.isEnabled = false
                addView(button)
            }
        }
        setBackgroundResource(backgroundId("${puzzleHeight}x${puzzleWidth}"))
        invalidate()
        requestLayout()
    }

    fun makeFill(row: Int, col: Int) {
        val selected =
            (context as SudokuActivity).findViewById<KeyboardView>(R.id.keyboard_view)
                .selected
        if (selected > 0) {
            val filler = Filler(row, col, selected)
            sudoku.fill(filler)
            updateSudokuData()
        }
        if (sudoku.isFull && sudoku.isValid) (context as SudokuActivity).end(basePuzzle)
    }

    fun removeFill() {
        if (sudoku.fillers.size > 0) {
            sudoku = sudoku.undoFill()
            updateSudokuData()
        }
    }

    fun solve() {
        val solver = LinearSolver()
        sudoku = solver.solve(Sudoku(basePuzzle))[0]
        updateSudokuData()
    }

    fun restart() {
        sudoku = Sudoku(basePuzzle)
        updateSudokuData()
    }

    fun load(index: Int?) {
        if (index != null) {
            val fillers = sudoku.fillers.subList(0, index)
            update(fillers)
        }
    }

    private fun updateSudokuData() {
        val fragment =
            (context as SudokuActivity).supportFragmentManager
                .findFragmentById(R.id.fragment_container)
        if (fragment != null) {
            val model = ViewModelProviders.of(fragment).get(SudokuViewModel::class.java)
            model.updateSudokuData(sudoku)
        }
    }

    fun update(fillers: List<Filler>?) {
        if (fillers != null) {
            sudoku = Sudoku.load(basePuzzle, fillers)
            for (i in 0 until rowCount) {
                for (j in 0 until columnCount) {
                    val button =
                        findViewWithTag<Button>(buttonTagFromPosition(i, j))
                    val value = sudoku.getValue(i, j)
                    val baseValue = basePuzzle.getNumber(i, j)
                    if (value != 0) {
                        button.text = numToText(value)
                        button.isEnabled = false
                    } else {
                        button.text = ""
                        button.isEnabled  = true
                    }
                    if (baseValue > 0) button.setTextColor(Color.BLACK)
                    if (baseValue == 0 && value > 0) button.setTextColor(Color.BLUE)
                    if (sudoku.isCracked(i, j)) button.setTextColor(Color.RED)
                }
            }
        }
    }


}