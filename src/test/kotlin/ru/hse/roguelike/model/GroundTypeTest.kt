package ru.hse.roguelike.model

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class GroundTypeTest {

    @Test
    fun `test display name`() {
        assertEquals("Огонь", GroundType.Fire.toString())
        assertEquals("Конец уровня", GroundType.LevelEnd.toString())
    }
}
