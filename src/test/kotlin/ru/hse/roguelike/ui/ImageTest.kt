package ru.hse.roguelike.ui

import com.googlecode.lanterna.TextCharacter
import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.graphics.BasicTextImage
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import ru.hse.roguelike.property.ColorProperties
import kotlin.test.assertEquals

internal class ImageTest {

    @Test
    fun `test set`() {
        val columns = 3
        val rows = 3
        val delegate = BasicTextImage(columns, rows)
        val image: Drawable = Image(delegate)
        assertEquals(columns, image.width)
        assertEquals(rows, image.height)
        image.set(0, 0, '#', Color.ANSI.Yellow, Color.ANSI.RedBright)
        assertEquals(
            TextCharacter.fromCharacter('#', TextColor.ANSI.YELLOW, TextColor.ANSI.RED_BRIGHT)[0],
            delegate.getCharacterAt(0, 0)
        )
        image.set(0, 0, 'A', Color.ANSI.Green, Color.ANSI.Green)
        assertEquals(
            TextCharacter.fromCharacter('A', TextColor.ANSI.GREEN, TextColor.ANSI.GREEN)[0],
            delegate.getCharacterAt(0, 0)
        )
    }

    @Test
    fun `test clear`() {
        val columns = 3
        val rows = 3
        val delegate = BasicTextImage(columns, rows)
        val image: Drawable = Image(delegate)
        image.set(0, 0, 'A', Color.ANSI.Green, Color.ANSI.Green)
        image.set(1, 1, 'A', Color.ANSI.Green, Color.ANSI.Green)
        image.set(2, 2, 'A', Color.ANSI.Green, Color.ANSI.Green)

        image.clear()
        for (y in 0 until rows) {
            for (x in 0 until columns) {
                assertEquals(
                    TextCharacter.fromCharacter(
                        ' ',
                        ColorProperties.textColor.textColor,
                        ColorProperties.defaultColor.textColor
                    )[0],
                    delegate.getCharacterAt(x, y)
                )
            }
        }
    }

    @Test
    fun `test fill`() {
        val columns = 3
        val rows = 3
        val delegate = BasicTextImage(columns, rows)
        val image: Drawable = Image(delegate)
        image.fill('A', Color.ANSI.Black, Color.ANSI.White)
        for (y in 0 until rows) {
            for (x in 0 until columns) {
                assertEquals(
                    TextCharacter.fromCharacter(
                        'A',
                        TextColor.ANSI.BLACK,
                        TextColor.ANSI.WHITE
                    )[0],
                    delegate.getCharacterAt(x, y)
                )
            }
        }
    }

    @Test
    fun `test set line`() {
        val columns = 8
        val rows = 3
        val delegate = BasicTextImage(columns, rows)
        val image: Drawable = Image(delegate).apply { fill(' ') }

        assertEquals(1, image.setLine(0, 0, ""))
        assertEquals(
            TextCharacter.fromCharacter(
                ' ',
                ColorProperties.textColor.textColor,
                ColorProperties.defaultColor.textColor
            )[0],
            delegate.getCharacterAt(0, 0)
        )

        assertEquals(1, image.setLine(0, 0, "a b", Color.ANSI.Black, Color.ANSI.White))
        for ((i, c) in "a b".withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(c, TextColor.ANSI.BLACK, TextColor.ANSI.WHITE)[0],
                delegate.getCharacterAt(i, 0)
            )
        }

        assertEquals(1, image.setLine(0, 0, "Hello", Color.ANSI.Black, Color.ANSI.White))
        for ((i, c) in "Hello".withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(c, TextColor.ANSI.BLACK, TextColor.ANSI.WHITE)[0],
                delegate.getCharacterAt(i, 0)
            )
        }

        assertEquals(2, image.setLine(1, 1, "Hello World"))
        for ((i, c) in "Hello".withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(
                    c,
                    ColorProperties.textColor.textColor,
                    ColorProperties.defaultColor.textColor
                )[0],
                delegate.getCharacterAt(i + 1, 1)
            )
        }
        for ((i, c) in "World".withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(
                    c,
                    ColorProperties.textColor.textColor,
                    ColorProperties.defaultColor.textColor
                )[0],
                delegate.getCharacterAt(i + 1, 2)
            )
        }
    }

    @Test
    fun `test set text`() {
        val columns = 8
        val rows = 3
        val delegate = BasicTextImage(columns, rows)
        val image: Drawable = Image(delegate)
        assertEquals(3, image.setText(0, 0, "Hello\nHello World", Color.ANSI.Black, Color.ANSI.White))
        for ((i, c) in "Hello".withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(c, TextColor.ANSI.BLACK, TextColor.ANSI.WHITE)[0],
                delegate.getCharacterAt(i, 0)
            )
        }
        for ((i, c) in "Hello".withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(c, TextColor.ANSI.BLACK, TextColor.ANSI.WHITE)[0],
                delegate.getCharacterAt(i, 1)
            )
        }
        for ((i, c) in "World".withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(c, TextColor.ANSI.BLACK, TextColor.ANSI.WHITE)[0],
                delegate.getCharacterAt(i, 2)
            )
        }
    }

    @Test
    fun `test set wrong width or height`() {
        val columns = 3
        val rows = 3
        val delegate = BasicTextImage(columns, rows)
        val image: Drawable = Image(delegate)

        assertDoesNotThrow { image.setLine(0, 0, "abc") }
        assertDoesNotThrow { image.setLine(2, 0, "a") }

        assertThrows<IllegalArgumentException> { image.setLine(0, 0, "abcd") }
        assertThrows<IllegalArgumentException> { image.setLine(4, 0, "a") }
        assertThrows<IllegalArgumentException> { image.setLine(0, 4, "a") }

        assertThrows<IllegalArgumentException> { image.setText(0, 0, "abcd") }
        assertThrows<IllegalArgumentException> { image.setText(4, 0, "a") }
        assertThrows<IllegalArgumentException> { image.setText(0, 4, "a") }

        assertThrows<IllegalArgumentException> { image.set(-1, 0, 'a') }
        assertThrows<IllegalArgumentException> { image.set(0, 5, 'a') }
    }
}
