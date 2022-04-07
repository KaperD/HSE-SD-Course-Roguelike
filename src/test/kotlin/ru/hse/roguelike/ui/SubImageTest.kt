package ru.hse.roguelike.ui

import com.googlecode.lanterna.TextCharacter
import com.googlecode.lanterna.TextColor
import com.googlecode.lanterna.graphics.BasicTextImage
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import ru.hse.roguelike.property.ColorProperties
import kotlin.test.assertEquals

internal class SubImageTest {

    @Test
    fun `test set`() {
        val columns = 3
        val rows = 3
        val delegate = BasicTextImage(columns, rows)
        val image: Drawable = Image(delegate).subImage(1, 1, 2, 2)
        assertEquals(2, image.width)
        assertEquals(2, image.height)
        image.set(0, 0, '#', Color.ANSI.Yellow, Color.ANSI.RedBright)
        assertEquals(
            TextCharacter.fromCharacter('#', TextColor.ANSI.YELLOW, TextColor.ANSI.RED_BRIGHT)[0],
            delegate.getCharacterAt(1, 1)
        )
        image.set(0, 0, 'A', Color.ANSI.Green, Color.ANSI.Green)
        assertEquals(
            TextCharacter.fromCharacter('A', TextColor.ANSI.GREEN, TextColor.ANSI.GREEN)[0],
            delegate.getCharacterAt(1, 1)
        )
    }

    @Test
    fun `test clear`() {
        val columns = 3
        val rows = 3
        val delegate = BasicTextImage(columns, rows)
        val image: Drawable = Image(delegate).apply { fill('A') }.subImage(1, 1, 2, 2)
        image.set(0, 0, 'A', Color.ANSI.Green, Color.ANSI.Green)
        image.set(1, 1, 'A', Color.ANSI.Green, Color.ANSI.Green)

        image.clear()
        for (i in 0 until 3) {
            assertEquals(
                TextCharacter.fromCharacter(
                    'A',
                    ColorProperties.textColor.textColor,
                    ColorProperties.defaultColor.textColor
                )[0],
                delegate.getCharacterAt(i, 0)
            )
            assertEquals(
                TextCharacter.fromCharacter(
                    'A',
                    ColorProperties.textColor.textColor,
                    ColorProperties.defaultColor.textColor
                )[0],
                delegate.getCharacterAt(0, i)
            )
        }
        for (y in 1 until rows) {
            for (x in 1 until columns) {
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
        val image: Drawable = Image(delegate).apply { fill('A') }.subImage(1, 1, 2, 2)
        image.fill('B', Color.ANSI.Black, Color.ANSI.White)
        for (i in 0 until 3) {
            assertEquals(
                TextCharacter.fromCharacter(
                    'A',
                    ColorProperties.textColor.textColor,
                    ColorProperties.defaultColor.textColor
                )[0],
                delegate.getCharacterAt(i, 0)
            )
            assertEquals(
                TextCharacter.fromCharacter(
                    'A',
                    ColorProperties.textColor.textColor,
                    ColorProperties.defaultColor.textColor
                )[0],
                delegate.getCharacterAt(0, i)
            )
        }
        for (y in 1 until rows) {
            for (x in 1 until columns) {
                assertEquals(
                    TextCharacter.fromCharacter(
                        'B',
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
        val rows = 4
        val delegate = BasicTextImage(columns, rows)
        val image: Drawable = Image(delegate).apply { fill(' ') }.subImage(1, 1, 6, 2)

        image.setLine(0, 0, "")
        assertEquals(
            TextCharacter.fromCharacter(
                ' ',
                ColorProperties.textColor.textColor,
                ColorProperties.defaultColor.textColor
            )[0],
            delegate.getCharacterAt(1, 1)
        )

        image.setLine(0, 0, "Hello", Color.ANSI.Black, Color.ANSI.White)
        for ((i, c) in "Hello".withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(c, TextColor.ANSI.BLACK, TextColor.ANSI.WHITE)[0],
                delegate.getCharacterAt(i + 1, 1)
            )
        }

        image.setLine(1, 1, "Hello World")
        for ((i, c) in "Hello".withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(
                    c,
                    ColorProperties.textColor.textColor,
                    ColorProperties.defaultColor.textColor
                )[0],
                delegate.getCharacterAt(i + 2, 2)
            )
        }
        for ((i, c) in "World".withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(
                    c,
                    ColorProperties.textColor.textColor,
                    ColorProperties.defaultColor.textColor
                )[0],
                delegate.getCharacterAt(i + 2, 3)
            )
        }
    }

    @Test
    fun `test set text`() {
        val columns = 8
        val rows = 3
        val delegate = BasicTextImage(columns, rows)
        val image: Drawable = Image(delegate).subImage(2, 0, 6, 3)
        image.setText(0, 0, "Hello\nHello World", Color.ANSI.Black, Color.ANSI.White)
        for ((i, c) in "Hello".withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(c, TextColor.ANSI.BLACK, TextColor.ANSI.WHITE)[0],
                delegate.getCharacterAt(i + 2, 0)
            )
        }
        for ((i, c) in "Hello".withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(c, TextColor.ANSI.BLACK, TextColor.ANSI.WHITE)[0],
                delegate.getCharacterAt(i + 2, 1)
            )
        }
        for ((i, c) in "World".withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(c, TextColor.ANSI.BLACK, TextColor.ANSI.WHITE)[0],
                delegate.getCharacterAt(i + 2, 2)
            )
        }
    }

    @Test
    fun `test set wrong width or height`() {
        val columns = 5
        val rows = 5
        val delegate = BasicTextImage(columns, rows)
        val image: Drawable = Image(delegate).subImage(1, 1, 2, 2)

        assertDoesNotThrow { image.setLine(0, 0, "ab") }
        assertDoesNotThrow { image.setLine(1, 0, "a") }

        assertThrows<IllegalArgumentException> { image.setLine(0, 0, "abc") }
        assertThrows<IllegalArgumentException> { image.setLine(2, 0, "a") }
        assertThrows<IllegalArgumentException> { image.setLine(0, 2, "a") }

        assertThrows<IllegalArgumentException> { image.setText(0, 0, "abc") }
        assertThrows<IllegalArgumentException> { image.setText(2, 0, "a") }
        assertThrows<IllegalArgumentException> { image.setText(0, 2, "a") }

        assertThrows<IllegalArgumentException> { image.set(-1, 0, 'a') }
        assertThrows<IllegalArgumentException> { image.set(0, 5, 'a') }

        assertThrows<IllegalArgumentException> { image.subImage(2, 0, 1, 1) }
        assertThrows<IllegalArgumentException> { image.subImage(0, 2, 1, 1) }
        assertThrows<IllegalArgumentException> { image.subImage(0, 0, 10, 1) }
        assertThrows<IllegalArgumentException> { image.subImage(0, 0, 1, 10) }
    }
}
