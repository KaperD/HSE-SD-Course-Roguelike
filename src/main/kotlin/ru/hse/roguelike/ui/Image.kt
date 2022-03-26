package ru.hse.roguelike.ui

import com.googlecode.lanterna.graphics.TextImage

class Image(private val delegate: TextImage) : SubImage(
    delegate,
    0,
    0,
    delegate.size.columns,
    delegate.size.rows
), TextImage by delegate  {

    fun subImage(topLeftX: Int, topLeftY: Int, width: Int, height: Int): Drawable {
        TODO("Not yet implemented")
    }
}

open class SubImage(
    private val delegate: TextImage,
    val topLeftX: Int,
    val topLeftY: Int,
    val width: Int,
    val height: Int
) : Drawable {

    override fun set(x: Int, y: Int, symbol: Char, foreground: Color, background: Color) {
        TODO("Not yet implemented")
    }

    override fun setLine(x: Int, y: Int, line: String, foreground: Color, background: Color) {
        TODO("Not yet implemented")
    }

    override fun setText(x: Int, y: Int, text: String, foreground: Color, background: Color) {
        TODO("Not yet implemented")
    }

    override fun clear() {
        TODO("Not yet implemented")
    }

}
