package com.panril.psudoku.db

import androidx.room.*


@Entity(tableName="puzzles")
data class SolvedPuzzle (
    @PrimaryKey(autoGenerate=true) val uid: Int = 0,
    val height: Int = 3,
    val width: Int = 3,
    val diff: Int,
    val time: Long,
    val state: String
)

@Dao
interface PuzzleDao {

    @Insert
    fun insert(puzzle: SolvedPuzzle)

    @Query("UPDATE puzzles SET time=:time WHERE uid=:uid")
    fun update(uid: Int, time: Long)

    @Query("SELECT * FROM puzzles WHERE state=:state")
    fun getPuzzleByState(state: String): SolvedPuzzle?

    @Query("SELECT * FROM puzzles")
    fun getAllSolved(): List<SolvedPuzzle>
}

@Database(entities = arrayOf(SolvedPuzzle::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun puzzleDao(): PuzzleDao
}