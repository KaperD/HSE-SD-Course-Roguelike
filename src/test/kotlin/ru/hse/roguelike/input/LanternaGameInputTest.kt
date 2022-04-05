package ru.hse.roguelike.input

import com.googlecode.lanterna.input.KeyStroke
import com.googlecode.lanterna.input.KeyType
import com.googlecode.lanterna.terminal.Terminal
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class LanternaGameInputTest {

    @Test
    fun `test get input`() {
        val terminal = mockk<Terminal>()
        every { terminal.readInput() } returns KeyStroke(KeyType.ArrowUp)
        val gameInput = LanternaGameInput(terminal)
        assertEquals(InputType.ArrowUp, gameInput.getInput())
        verify { terminal.readInput() }
    }
}
