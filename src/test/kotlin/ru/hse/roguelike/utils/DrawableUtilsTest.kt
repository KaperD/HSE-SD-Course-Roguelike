package ru.hse.roguelike.utils

import com.googlecode.lanterna.TextCharacter
import com.googlecode.lanterna.graphics.BasicTextImage
import org.junit.jupiter.api.Test
import ru.hse.roguelike.property.ColorProperties
import ru.hse.roguelike.ui.Drawable
import ru.hse.roguelike.ui.Image
import kotlin.test.assertEquals

internal class DrawableUtilsTest {

    @Test
    fun `test append line`() {
        val columns = 8
        val rows = 4
        val delegate = BasicTextImage(columns, rows)
        val image: Drawable = Image(delegate).apply { fill(' ') }
        image.drawText {
            appendLine("Hello World")
            appendLine("")
            appendLine("World")
        }
        for ((i, c) in "Hello".withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(
                    c,
                    ColorProperties.textColor.textColor,
                    ColorProperties.defaultColor.textColor
                )[0],
                delegate.getCharacterAt(i, 0)
            )
        }
        for ((i, c) in "World".withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(
                    c,
                    ColorProperties.textColor.textColor,
                    ColorProperties.defaultColor.textColor
                )[0],
                delegate.getCharacterAt(i, 1)
            )
        }
        for (i in 0 until columns) {
            assertEquals(
                TextCharacter.fromCharacter(
                    ' ',
                    ColorProperties.textColor.textColor,
                    ColorProperties.defaultColor.textColor
                )[0],
                delegate.getCharacterAt(i, 2)
            )
        }
        for ((i, c) in "World".withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(
                    c,
                    ColorProperties.textColor.textColor,
                    ColorProperties.defaultColor.textColor
                )[0],
                delegate.getCharacterAt(i, 3)
            )
        }
    }

    @Test
    fun `test append title`() {
        val columns = 8
        val rows = 4
        val delegate = BasicTextImage(columns, rows)
        val image: Drawable = Image(delegate).apply { fill(' ') }
        image.drawText {
            appendTitle("Hello World")
            appendTitle("")
            appendTitle("World")
        }
        for ((i, c) in "Hello".withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(
                    c,
                    ColorProperties.titleColor.textColor,
                    ColorProperties.defaultColor.textColor
                )[0],
                delegate.getCharacterAt(i, 0)
            )
        }
        for ((i, c) in "World".withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(
                    c,
                    ColorProperties.titleColor.textColor,
                    ColorProperties.defaultColor.textColor
                )[0],
                delegate.getCharacterAt(i, 1)
            )
        }
        for (i in 0 until columns) {
            assertEquals(
                TextCharacter.fromCharacter(
                    ' ',
                    ColorProperties.textColor.textColor,
                    ColorProperties.defaultColor.textColor
                )[0],
                delegate.getCharacterAt(i, 2)
            )
        }
        for ((i, c) in "World".withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(
                    c,
                    ColorProperties.titleColor.textColor,
                    ColorProperties.defaultColor.textColor
                )[0],
                delegate.getCharacterAt(i, 3)
            )
        }
    }

    @Test
    fun `test append text`() {
        val columns = 8
        val rows = 4
        val delegate = BasicTextImage(columns, rows)
        val image: Drawable = Image(delegate).apply { fill(' ') }
        image.drawText {
            appendText("Hello")
            appendText("Hello World\nW")
        }
        for ((i, c) in "Hello".withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(
                    c,
                    ColorProperties.textColor.textColor,
                    ColorProperties.defaultColor.textColor
                )[0],
                delegate.getCharacterAt(i, 0)
            )
        }
        for ((i, c) in "Hello".withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(
                    c,
                    ColorProperties.textColor.textColor,
                    ColorProperties.defaultColor.textColor
                )[0],
                delegate.getCharacterAt(i, 1)
            )
        }
        for ((i, c) in "World".withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(
                    c,
                    ColorProperties.textColor.textColor,
                    ColorProperties.defaultColor.textColor
                )[0],
                delegate.getCharacterAt(i, 2)
            )
        }
        for ((i, c) in "W".withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(
                    c,
                    ColorProperties.textColor.textColor,
                    ColorProperties.defaultColor.textColor
                )[0],
                delegate.getCharacterAt(i, 3)
            )
        }
    }
}
