package ru.hse.roguelike.state

import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import ru.hse.roguelike.input.InputType
import ru.hse.roguelike.sound.GameSound
import ru.hse.roguelike.ui.help.MessageView
import kotlin.test.assertEquals

internal class HelpStateTest {

    @Test
    fun `test help state`() {
        val messageView = mockk<MessageView>(relaxed = true)
        val gameSound = mockk<GameSound>(relaxed = true)
        val anotherState = mockk<State>()
        val helpState = HelpState(messageView, gameSound, mapOf(InputType.I to anotherState))
        verify { messageView.setText("Правила${System.lineSeparator()}игры") }
        confirmVerified(messageView)

        helpState.activate()
        verify { messageView.show() }
        confirmVerified(messageView)

        assertEquals(helpState, helpState.handleInput(InputType.M))
        assertEquals(anotherState, helpState.handleInput(InputType.I))
        verify(exactly = 1) { gameSound.beep() }
        confirmVerified(gameSound)
    }
}
