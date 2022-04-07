package ru.hse.roguelike.ui.window

import com.googlecode.lanterna.TerminalPosition
import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.TextCharacter
import com.googlecode.lanterna.graphics.TextGraphics
import com.googlecode.lanterna.graphics.TextImage
import com.googlecode.lanterna.terminal.Terminal
import com.googlecode.lanterna.terminal.TerminalResizeListener
import io.mockk.*
import org.junit.jupiter.api.Test
import ru.hse.roguelike.property.ColorProperties
import ru.hse.roguelike.property.StringProperties
import kotlin.test.assertEquals

internal class LanternaGameWindowTest {

    @Test
    fun `game window test`() {
        val textGraphics = mockk<TextGraphics>(relaxed = true)
        val terminal = mockk<Terminal>(relaxed = true)
        every { terminal.newTextGraphics() } returns textGraphics
        every { terminal.terminalSize } returns TerminalSize(50, 50)

        val width = 40
        val height = 30
        val window = LanternaGameWindow(terminal, width, height)
        verify { terminal.terminalSize }
        verify { terminal.setCursorVisible(false) }
        verify { terminal.addResizeListener(any()) }
        verify { terminal.newTextGraphics() }
        verify { terminal.clearScreen() }
        verify { terminal.resetColorAndSGR() }
        verify { terminal.flush() }
        confirmVerified(terminal)
        verify { textGraphics.drawImage(TerminalPosition.TOP_LEFT_CORNER, any()) }
        verify { textGraphics.drawImage(TerminalPosition(1, 1), any()) }
        confirmVerified(textGraphics)

        val image = window.createImage()
        assertEquals(width, image.width)
        assertEquals(height, image.height)

        window.show(image)
        verify { terminal.flush() }
        confirmVerified(terminal)
        verify { textGraphics.drawImage(TerminalPosition(1, 1), any()) }
        confirmVerified(textGraphics)
    }

    @Test
    fun `game window small size test`() {
        val textGraphics = mockk<TextGraphics>(relaxed = true)
        val textImage = slot<TextImage>()
        every { textGraphics.drawImage(TerminalPosition(1, 1), capture(textImage)) } returns textGraphics
        val terminal = mockk<Terminal>(relaxed = true)
        every { terminal.newTextGraphics() } returns textGraphics
        every { terminal.terminalSize } returns TerminalSize(10, 10)

        val width = 40
        val height = 30
        val window = LanternaGameWindow(terminal, width, height)
        verify { terminal.terminalSize }
        verify { terminal.setCursorVisible(false) }
        verify { terminal.addResizeListener(any()) }
        verify { terminal.newTextGraphics() }
        verify { terminal.clearScreen() }
        verify { terminal.resetColorAndSGR() }
        verify { terminal.flush() }
        confirmVerified(terminal)
        verify { textGraphics.drawImage(TerminalPosition.TOP_LEFT_CORNER, any()) }
        verify { textGraphics.drawImage(TerminalPosition(1, 1), any()) }
        confirmVerified(textGraphics)

        for ((i, c) in StringProperties.makeWindowBigger.withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(
                    c,
                    ColorProperties.textColor.textColor,
                    ColorProperties.defaultColor.textColor
                )[0],
                textImage.captured.getCharacterAt(i, 0)
            )
        }

        val image = window.createImage()
        assertEquals(width, image.width)
        assertEquals(height, image.height)

        window.show(image)
        confirmVerified(terminal)
        confirmVerified(textGraphics)
    }

    @Test
    fun `game window resize test`() {
        val textGraphics = mockk<TextGraphics>(relaxed = true)
        val textImage = slot<TextImage>()
        every { textGraphics.drawImage(TerminalPosition(1, 1), capture(textImage)) } returns textGraphics
        val terminal = mockk<Terminal>(relaxed = true)
        val listener = slot<TerminalResizeListener>()
        every { terminal.newTextGraphics() } returns textGraphics
        every { terminal.terminalSize } returns TerminalSize(10, 10)
        every { terminal.addResizeListener(capture(listener)) } answers { }

        val width = 40
        val height = 30
        val window = LanternaGameWindow(terminal, width, height)
        verify { terminal.terminalSize }
        verify { terminal.setCursorVisible(false) }
        verify { terminal.addResizeListener(any()) }
        verify { terminal.newTextGraphics() }
        verify { terminal.clearScreen() }
        verify { terminal.resetColorAndSGR() }
        verify { terminal.flush() }
        confirmVerified(terminal)
        verify { textGraphics.drawImage(TerminalPosition.TOP_LEFT_CORNER, any()) }
        verify { textGraphics.drawImage(TerminalPosition(1, 1), any()) }
        confirmVerified(textGraphics)

        for ((i, c) in StringProperties.makeWindowBigger.withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(
                    c,
                    ColorProperties.textColor.textColor,
                    ColorProperties.defaultColor.textColor
                )[0],
                textImage.captured.getCharacterAt(i, 0)
            )
        }

        listener.captured.onResized(terminal, TerminalSize(100, 100))
        for ((i, _) in StringProperties.makeWindowBigger.withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(
                    ' ',
                    ColorProperties.textColor.textColor,
                    ColorProperties.defaultColor.textColor
                )[0],
                textImage.captured.getCharacterAt(i, 0)
            )
        }
        verify { terminal.newTextGraphics() }
        verify { terminal.clearScreen() }
        verify { terminal.resetColorAndSGR() }
        verify { terminal.flush() }
        confirmVerified(terminal)
        verify { textGraphics.drawImage(TerminalPosition.TOP_LEFT_CORNER, any()) }
        verify { textGraphics.drawImage(TerminalPosition(1, 1), any()) }
        confirmVerified(textGraphics)

        listener.captured.onResized(terminal, TerminalSize(10, 10))
        for ((i, c) in StringProperties.makeWindowBigger.withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(
                    c,
                    ColorProperties.textColor.textColor,
                    ColorProperties.defaultColor.textColor
                )[0],
                textImage.captured.getCharacterAt(i, 0)
            )
        }
        verify { terminal.newTextGraphics() }
        verify { terminal.clearScreen() }
        verify { terminal.resetColorAndSGR() }
        verify { terminal.flush() }
        confirmVerified(terminal)
        verify { textGraphics.drawImage(TerminalPosition.TOP_LEFT_CORNER, any()) }
        verify { textGraphics.drawImage(TerminalPosition(1, 1), any()) }
        confirmVerified(textGraphics)
    }
}
