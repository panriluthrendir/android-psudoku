package com.panril.psudoku.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.ContextThemeWrapper
import android.widget.Button
import android.widget.LinearLayout
import com.panril.psudoku.R
import com.panril.psudoku.util.keyboardIndexFromTag
import com.panril.psudoku.util.keyboardTagFromIndex
import com.panril.psudoku.util.numToText


class KeyboardView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    var selected = 0
    var size = 9

    set(value) {
        field = value
        update()
    }

    init {
        update()
        setBackgroundColor(
            resources.getColor(R.color.sudoku_background, null)
        )
    }

    fun update() {
        removeAllViews()
        for (i in 1..size) {
            val themeContext = ContextThemeWrapper(context, R.style.SudokuButton)
            val button = Button(context)
            val params = LayoutParams(themeContext, null)
            button.layoutParams = params
            button.text = numToText(i)
            button.tag = keyboardTagFromIndex(i)
            button.setOnClickListener { v  -> select(v as Button) }
            addView(button)
        }
        invalidate()
        requestLayout()
    }

    private fun select(button: Button) {
        Log.e("button_param", button.layoutParams.width.toString())
        Log.e("button_width", button.width.toString())
        if (selected > 0) {
            findViewWithTag<Button>(keyboardTagFromIndex(selected))
                .setTextColor(Color.BLACK)
        }
        selected = keyboardIndexFromTag(button.tag.toString())
        button.setTextColor(Color.BLUE)
    }

}