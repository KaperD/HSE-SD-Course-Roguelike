package ru.hse.roguelike.ui

import ru.hse.roguelike.property.ColorProperties.defaultColor
import ru.hse.roguelike.property.ColorProperties.textColor

interface Drawable {
    val width: Int
    val height: Int

    fun set(x: Int, y: Int, symbol: Char, foreground: Color = textColor, background: Color = defaultColor)
    fun setLine(x: Int, y: Int, line: String, foreground: Color = textColor, background: Color = defaultColor)
    fun setText(x: Int, y: Int, text: String, foreground: Color = textColor, background: Color = defaultColor)
    fun subImage(topLeftX: Int, topLeftY: Int, width: Int, height: Int): Drawable
    fun fill(symbol: Char, foreground: Color = textColor, background: Color = defaultColor)
    fun clear()
}
