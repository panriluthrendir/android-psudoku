package com.panril.psudoku.activity

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.panril.psudoku.R
import com.panril.psudoku.db.AppDatabase
import com.panril.psudoku.db.SolvedPuzzle
import com.panril.psudoku.util.PuzzleAdapter

class PuzzleListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puzzle_list)
        DatabaseAccess(this).execute()
    }

    class DatabaseAccess(val activity: PuzzleListActivity) :
        AsyncTask<Void, Void, List<SolvedPuzzle>>() {

        override fun doInBackground(vararg params: Void?): List<SolvedPuzzle> {
            val db =  Room.databaseBuilder(
                activity.applicationContext,
                AppDatabase::class.java, "puzzles"
            ).build()
            return db.puzzleDao().getAllSolved()
        }

        override fun onPostExecute(result: List<SolvedPuzzle>?) {
            val recyclerView = activity.findViewById<RecyclerView>(R.id.rv_puzzles)
            if (result != null) {
                val manager = LinearLayoutManager(activity)
                recyclerView.layoutManager = manager
                recyclerView.adapter = PuzzleAdapter(result, activity)
            } else {
                recyclerView.adapter = PuzzleAdapter(emptyList(), activity)
            }
        }
    }

    fun onClick(view: View) {

    }
}
