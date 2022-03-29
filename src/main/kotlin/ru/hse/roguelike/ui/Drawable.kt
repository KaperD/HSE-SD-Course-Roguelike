package ru.hse.roguelike.ui

import ru.hse.roguelike.property.ColorProperties.defaultColor
import ru.hse.roguelike.property.ColorProperties.textColor

interface Drawable {
    val width: Int
    val height: Int

    fun set(x: Int, y: Int, symbol: Char, foreground: Color = textColor, background: Color = defaultColor)

    /**
     * @return number of image lines taken to draw given line
     */
    fun drawLine(x: Int, y: Int, line: String, foreground: Color = textColor, background: Color = defaultColor): Int

    /**
     * @return number of image lines taken to draw given line
     */
    fun drawText(x: Int, y: Int, text: String, foreground: Color = textColor, background: Color = defaultColor): Int
    fun subImage(topLeftX: Int, topLeftY: Int, width: Int, height: Int): Drawable
    fun fill(symbol: Char, foreground: Color = textColor, background: Color = defaultColor)
    fun clear()
}
