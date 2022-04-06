package ru.hse.roguelike.state

import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

import ru.hse.roguelike.input.InputType
import ru.hse.roguelike.sound.GameSound
import ru.hse.roguelike.ui.help.MessageView
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class GameOverStateTest {

    @Test
    fun `test game over state`() {
        val messageView = mockk<MessageView>(relaxed = true)
        val gameSound = mockk<GameSound>(relaxed = true)
        val gameOverState = GameOverState(messageView, gameSound)
        verify { messageView.setText("Вы проиграли..\n:(") }
        confirmVerified(messageView)

        gameOverState.activate()
        verify { messageView.show() }
        confirmVerified(messageView)

        assertEquals(gameOverState, gameOverState.handleInput(InputType.I))
        assertEquals(gameOverState, gameOverState.handleInput(InputType.M))
        verify(exactly = 2) { gameSound.beep() }
        confirmVerified(gameSound)
    }
}
