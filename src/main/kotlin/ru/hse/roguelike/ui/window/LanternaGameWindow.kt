package ru.hse.roguelike.ui.window

import com.googlecode.lanterna.TerminalPosition
import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.TextCharacter
import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.graphics.BasicTextImage
import com.googlecode.lanterna.terminal.Terminal
import ru.hse.roguelike.property.ColorProperties.borderColor
import ru.hse.roguelike.property.ColorProperties.defaultColor
import ru.hse.roguelike.property.StringProperties.makeWindowBigger
import ru.hse.roguelike.ui.Image

class LanternaGameWindow(
    private val terminal: Terminal,
    val imageWidth: Int,
    val imageHeight: Int
) : GameWindow {
    private val width = imageWidth + 2
    private val height = imageHeight + 2
    private val backgroundImage = Image(
        BasicTextImage(
            TerminalSize(width, height),
            TextCharacter.fromCharacter(' ', TextColor.ANSI.DEFAULT, borderColor.textColor).first()
        )
    )
    private var textGraphics = terminal.newTextGraphics()
    private val currentImage = createImage()
    private val resizeMessageImage = createImage().also { it.drawLine(0, 0, makeWindowBigger) }
    private var currentSize = terminal.terminalSize

    init {
        terminal.setCursorVisible(false)
        terminal.addResizeListener { _, newSize ->
            redraw(newSize)
        }
        redraw(currentSize)
    }

    @Synchronized
    override fun show(image: Image) {
        image.copyTo(currentImage)
        if (currentSize.columns >= width && currentSize.rows >= height) {
            drawImage(currentImage)
            terminal.flush()
        }
    }

    override fun createImage(): Image = Image(
        BasicTextImage(
            TerminalSize(imageWidth, imageHeight),
            TextCharacter.fromCharacter(' ', TextColor.ANSI.DEFAULT, defaultColor.textColor).first()
        )
    )

    @Synchronized
    private fun redraw(newSize: TerminalSize) {
        currentSize = newSize
        terminal.resetColorAndSGR()
        terminal.clearScreen()
        textGraphics = terminal.newTextGraphics()
        textGraphics.drawImage(TerminalPosition.TOP_LEFT_CORNER, backgroundImage)
        if (currentSize.columns < width || currentSize.rows < height) {
            drawImage(resizeMessageImage)
        } else {
            drawImage(currentImage)
        }
        terminal.flush()
    }

    private fun drawImage(image: Image) {
        textGraphics.drawImage(topLeftCorner, image)
    }

    companion object {
        private val topLeftCorner = TerminalPosition(1, 1)
    }
}
