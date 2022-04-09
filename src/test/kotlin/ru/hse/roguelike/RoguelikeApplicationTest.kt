package ru.hse.roguelike

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import ru.hse.roguelike.input.GameInput
import ru.hse.roguelike.input.InputType
import ru.hse.roguelike.state.State

internal class RoguelikeApplicationTest {

    @Test
    fun `test run`() {
        val gameInput = mockk<GameInput>()
        every { gameInput.getInput() } returnsMany listOf(InputType.ArrowRight, InputType.I, InputType.Esc)
        val state1 = mockk<State>(relaxed = true)
        val state2 = mockk<State>(relaxed = true)
        every { state1.handleInput(InputType.ArrowRight) } returns state2
        every { state2.handleInput(InputType.I) } returns state2
        val application = RoguelikeApplication(gameInput, state1)
        application.run()

        verify { state1.activate() }
        verify { state1.handleInput(InputType.ArrowRight) }

        verify { state2.activate() }
        verify { state2.handleInput(InputType.I) }
    }
}
