package ru.hse.roguelike.state

import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import ru.hse.roguelike.input.InputType
import ru.hse.roguelike.sound.GameSound
import ru.hse.roguelike.ui.help.MessageView
import kotlin.test.assertEquals

internal class VictoryStateTest {

    @Test
    fun `test victory state`() {
        val messageView = mockk<MessageView>(relaxed = true)
        val gameSound = mockk<GameSound>(relaxed = true)
        val victoryState = VictoryState(messageView, gameSound)
        verify { messageView.setText("Вы победили!\n:)") }
        confirmVerified(messageView)

        victoryState.activate()
        verify { messageView.show() }
        confirmVerified(messageView)

        assertEquals(victoryState, victoryState.handleInput(InputType.I))
        assertEquals(victoryState, victoryState.handleInput(InputType.M))
        verify(exactly = 2) { gameSound.beep() }
        confirmVerified(gameSound)
    }
}
