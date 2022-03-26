package ru.hse.roguelike.ui

import ru.hse.roguelike.ui.Color.ANSI.Default
import ru.hse.roguelike.ui.Color.ANSI.White

interface Drawable {
    fun set(x: Int, y: Int, symbol: Char, foreground: Color = Default, background: Color = Default)
    fun setLine(x: Int, y: Int, line: String, foreground: Color = Default, background: Color = White)
    fun setText(x: Int, y: Int, text: String, foreground: Color = Default, background: Color = White)
    fun clear()
}
