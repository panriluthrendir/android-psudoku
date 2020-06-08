package com.panril.psudoku.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import sudoku.core.Filler
import sudoku.core.Sudoku


class SudokuViewModel : ViewModel() {

    private var sudokuData: MutableLiveData<List<Filler>>

    init {
        sudokuData = MutableLiveData()
        sudokuData.value = emptyList()
    }

    fun getSudokuData(): MutableLiveData<List<Filler>> {
        return sudokuData
    }

    fun updateSudokuData(sudoku: Sudoku) {
        sudokuData.value = sudoku.fillers
    }

}
