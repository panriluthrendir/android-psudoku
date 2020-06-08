package com.panril.psudoku.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.panril.psudoku.R

import sudoku.core.Filler


class SudokuFragment : Fragment() {

    companion object {
        fun newInstance() = SudokuFragment()
    }

    private lateinit var viewModel: SudokuViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sudoku, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(SudokuViewModel::class.java)
        viewModel.getSudokuData()
            .observe(this,
                Observer {
                        fillers: List<Filler>? ->
                            activity?.findViewById<SudokuView>(R.id.sudoku_view)
                                ?.update(fillers)
                }
            )
    }

}
