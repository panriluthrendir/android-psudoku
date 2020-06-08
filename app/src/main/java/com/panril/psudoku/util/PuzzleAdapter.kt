package com.panril.psudoku.util

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.puzzle_list_item.view.*
import com.panril.psudoku.R
import com.panril.psudoku.activity.SudokuActivity
import com.panril.psudoku.db.SolvedPuzzle
import sudoku.core.Sudoku
import sudoku.puzzle.Puzzle
import sudoku.solver.LinearSolver

class PuzzleAdapter(val puzzles: List<SolvedPuzzle>, val context: Context) :
        RecyclerView.Adapter<PuzzleAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater
                .from(context)
                .inflate(R.layout.puzzle_list_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return puzzles.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val solvedPuzzle = puzzles[position]
        val puzzle = puzzleFromString(solvedPuzzle.state)
        Log.e("puzzle", puzzle.toString())

        holder.sizeText.text = "size: ${solvedPuzzle.height}x${solvedPuzzle.width}"
        holder.diffText.text = "difficulty: ${solvedPuzzle.diff}"
        holder.timeText.text = "time: ${millisToTimeStr(solvedPuzzle.time)}"
        val state = solvedPuzzle.state
        holder.button.setOnClickListener({
            openOnActivity(state)
        })
    }

    private fun openOnActivity(state: String) {
        val intent = Intent(context, SudokuActivity::class.java)
        intent.putExtra("puzzle_state", state)
        context.startActivity(intent)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val sizeText = view.tv_item_size
        val diffText = view.tv_item_diff
        val timeText = view.tv_item_time
        val button = view.bt_try_again
    }
}