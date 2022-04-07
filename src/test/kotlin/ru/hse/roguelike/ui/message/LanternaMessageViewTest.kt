package ru.hse.roguelike.ui.message

import com.googlecode.lanterna.TextCharacter
import com.googlecode.lanterna.graphics.BasicTextImage
import io.mockk.*
import org.junit.jupiter.api.Test
import ru.hse.roguelike.property.ColorProperties
import ru.hse.roguelike.ui.Image
import ru.hse.roguelike.ui.window.GameWindow
import kotlin.test.assertEquals

internal class LanternaMessageViewTest {

    @Test
    fun `test message view`() {
        val width = 10
        val height = 10
        val baseImage = Image(BasicTextImage(width, height))
        val image = slot<Image>()
        val window = mockk<GameWindow>()
        every { window.createImage() } returns baseImage
        every { window.show(capture(image)) } answers { }

        val messageView = LanternaMessageView(window)
        verify { window.createImage() }
        confirmVerified(window)
        messageView.show()
        verify { window.show(baseImage) }
        confirmVerified(window)

        for (y in 0 until height) {
            for (x in 0 until width) {
                assertEquals(
                    TextCharacter.fromCharacter(
                        ' ',
                        ColorProperties.textColor.textColor,
                        ColorProperties.defaultColor.textColor
                    )[0],
                    image.captured.getCharacterAt(0, 0)
                )
            }
        }

        messageView.setText("Hello\nWo")
        messageView.show()
        verify { window.show(baseImage) }
        confirmVerified(window)
        for ((i, c) in "Hello".withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(
                    c,
                    ColorProperties.textColor.textColor,
                    ColorProperties.defaultColor.textColor
                )[0],
                image.captured.getCharacterAt(i + 2, 4)
            )
        }
        for ((i, c) in "Wo".withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(
                    c,
                    ColorProperties.textColor.textColor,
                    ColorProperties.defaultColor.textColor
                )[0],
                image.captured.getCharacterAt(i + 2, 5)
            )
        }
    }
}
