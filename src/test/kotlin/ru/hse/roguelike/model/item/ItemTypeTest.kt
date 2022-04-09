package ru.hse.roguelike.model.item

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class ItemTypeTest {

    @Test
    fun `test display name`() {
        assertEquals("Голова", ItemType.Head.toString())
        assertEquals("Одноразовый", ItemType.Disposable.toString())
    }
}
