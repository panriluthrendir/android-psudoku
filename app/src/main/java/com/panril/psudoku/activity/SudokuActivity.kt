package com.panril.psudoku.activity

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.SystemClock
import android.view.*
import android.widget.Chronometer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.panril.psudoku.view.KeyboardView
import com.panril.psudoku.R
import com.panril.psudoku.view.SudokuView
import com.panril.psudoku.db.AppDatabase
import com.panril.psudoku.db.SolvedPuzzle
import com.panril.psudoku.util.chronoTime
import com.panril.psudoku.util.generatePuzzleFromPreferences
import com.panril.psudoku.util.puzzleToString
import sudoku.core.Sudoku
import sudoku.puzzle.Puzzle
import sudoku.solver.LinearSolver


class SudokuActivity : AppCompatActivity() {

    private lateinit var sudokuView: SudokuView
    private lateinit var keyboardView: KeyboardView
    private lateinit var chrono: Chronometer
    private var savedIndex = 0
    private var buttonSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sudoku)
        setSupportActionBar(findViewById(R.id.toolbar))
        sudokuView = findViewById(R.id.sudoku_view)
        keyboardView = findViewById(R.id.keyboard_view)
        chrono = findViewById(R.id.chronometer)
        newPuzzle()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            true
        }
        R.id.action_new -> {
            newPuzzle()
            true
        }
        R.id.action_solvedlist -> {
            val intent = Intent(this, PuzzleListActivity::class.java)
            startActivity(intent)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun newPuzzle() {
        val puzzle = generatePuzzleFromPreferences(this)
        sudokuView.newPuzzle(puzzle)
        keyboardView.size = puzzle.height * puzzle.width
        chrono.base = SystemClock.elapsedRealtime()
        chrono.start()
    }

    fun end(basePuzzle: Puzzle) {
        Toast.makeText(this, "Congratulations, you solved this puzzle!", Toast.LENGTH_LONG)
            .show()
        chrono.stop()
        val diff = LinearSolver().difficulty(Sudoku(basePuzzle))
        val solvedPuzzle = SolvedPuzzle(
            time= chronoTime(chrono), diff=diff, state= puzzleToString(basePuzzle)
        )
        SaveToDatabase(this).execute(solvedPuzzle)
    }

    fun onClick(view: View) {
        when(view.id) {
            R.id.bt_back -> sudokuView.removeFill()
            R.id.bt_solve -> sudokuView.solve()
            R.id.bt_restart -> {
                sudokuView.restart()
                chrono.setBase(SystemClock.elapsedRealtime())
            }
            R.id.bt_save -> savedIndex = sudokuView.sudoku.fillers.size -1
            R.id.bt_load -> sudokuView.load(savedIndex)
        }
    }

    class SaveToDatabase(val activity: SudokuActivity) : AsyncTask<SolvedPuzzle, Void, Void>() {

        override fun doInBackground(vararg params: SolvedPuzzle?): Void? {
            val puzzle = params[0]
            val db = Room.databaseBuilder(
                activity.applicationContext,
                AppDatabase::class.java, "puzzles"
            ).build()
            if (puzzle != null) {
                db.puzzleDao().insert(puzzle)
            }
            return null
        }
    }

}
