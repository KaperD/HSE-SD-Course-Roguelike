package ru.hse.roguelike.input

import com.googlecode.lanterna.input.KeyStroke
import com.googlecode.lanterna.input.KeyType
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class InputTypeTest {

    @Test
    fun `test convert from string`() {
        assertEquals(InputType.ArrowRight, "ArrowRight".toInputType())
    }

    @Test
    fun `test convert from KeyType`() {
        assertEquals(InputType.M, KeyStroke('m', false, false).toInputType())
        assertEquals(InputType.F, KeyStroke('f', false, false).toInputType())
        assertEquals(InputType.I, KeyStroke('i', false, false).toInputType())
        assertEquals(InputType.H, KeyStroke('h', false, false).toInputType())
        assertEquals(InputType.ArrowUp, KeyStroke(KeyType.ArrowUp).toInputType())
        assertEquals(InputType.ArrowDown, KeyStroke(KeyType.ArrowDown).toInputType())
        assertEquals(InputType.ArrowLeft, KeyStroke(KeyType.ArrowLeft).toInputType())
        assertEquals(InputType.ArrowRight, KeyStroke(KeyType.ArrowRight).toInputType())
        assertEquals(InputType.Enter, KeyStroke(KeyType.Enter).toInputType())
        assertEquals(InputType.Esc, KeyStroke(KeyType.Escape).toInputType())
        assertEquals(InputType.Unknown, KeyStroke(KeyType.Unknown).toInputType())
        assertEquals(InputType.Unknown, KeyStroke('a', false, false).toInputType())
    }
}
