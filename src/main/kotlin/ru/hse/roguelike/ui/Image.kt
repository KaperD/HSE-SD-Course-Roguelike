package ru.hse.roguelike.ui

import com.googlecode.lanterna.TerminalPosition
import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.TextCharacter
import com.googlecode.lanterna.graphics.TextImage

class Image(private val delegate: TextImage) :
    SubImage(
        delegate,
        0,
        0,
        delegate.size.columns,
        delegate.size.rows
    ),
    TextImage by delegate

open class SubImage(
    private val delegate: TextImage,
    private val topLeftX: Int,
    private val topLeftY: Int,
    override val width: Int,
    override val height: Int
) : Drawable {
    private val textGraphics = delegate.newTextGraphics()

    override fun set(x: Int, y: Int, symbol: Char, foreground: Color, background: Color) {
        require(x in 0 until width && y in 0 until height)
        delegate.setCharacterAt(
            topLeftX + x,
            topLeftY + y,
            TextCharacter.fromCharacter(symbol, foreground.textColor, background.textColor).first()
        )
    }

    override fun drawLine(x: Int, y: Int, line: String, foreground: Color, background: Color): Int {
        require(x in 0 until width)
        textGraphics.foregroundColor = foreground.textColor
        textGraphics.backgroundColor = background.textColor
        var curX = x
        var curY = y
        val words = line.split(whiteSpaceRegex)
        val currentLineWords = mutableListOf<String>()
        for (word in words) {
            if (curX + word.length > width) {
                textGraphics.putString(
                    topLeftX + x,
                    topLeftY + curY,
                    currentLineWords.joinToString(separator = " ")
                )
                currentLineWords.clear()
                curX = x
                curY++
            }
            currentLineWords.add(word)
            curX += word.length + 1
        }
        if (currentLineWords.isNotEmpty()) {
            textGraphics.putString(
                topLeftX + x,
                topLeftY + curY,
                currentLineWords.joinToString(separator = " ")
            )
        }
        return curY - y + 1
    }

    override fun drawText(x: Int, y: Int, text: String, foreground: Color, background: Color): Int {
        var curY = y
        for (line in text.lines()) {
            curY += drawLine(x, curY, line, foreground, background)
        }
        return curY - y
    }

    override fun subImage(topLeftX: Int, topLeftY: Int, width: Int, height: Int): Drawable {
        require(topLeftX in 0 until this.width && topLeftY in 0 until this.height)
        require(width > 0 && topLeftX + width <= this.width)
        require(height > 0 && topLeftY + height <= this.height)
        return SubImage(delegate, this.topLeftX + topLeftX, this.topLeftY + topLeftY, width, height)
    }

    override fun fill(symbol: Char, foreground: Color, background: Color) {
        val s = TextCharacter.fromCharacter(symbol, foreground.textColor, background.textColor).first()
        textGraphics.fillRectangle(TerminalPosition(topLeftX, topLeftY), TerminalSize(width, height), s)
    }

    override fun clear() {
        fill(' ')
    }

    companion object {
        val whiteSpaceRegex = "\\s".toRegex()
    }
}
