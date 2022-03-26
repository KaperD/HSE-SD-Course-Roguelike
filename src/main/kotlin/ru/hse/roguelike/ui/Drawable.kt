package ru.hse.roguelike.ui

import ru.hse.roguelike.ui.Color.ANSI.*

interface Drawable {
    val width: Int
    val height: Int

    fun set(x: Int, y: Int, symbol: Char, foreground: Color = Black, background: Color = Default)
    fun setLine(x: Int, y: Int, line: String, foreground: Color = Black, background: Color = Default)
    fun setText(x: Int, y: Int, text: String, foreground: Color = Black, background: Color = Default)
    fun subImage(topLeftX: Int, topLeftY: Int, width: Int, height: Int): Drawable
    fun fill(symbol: Char, foreground: Color = Black, background: Color = Default)
    fun clear()
}
