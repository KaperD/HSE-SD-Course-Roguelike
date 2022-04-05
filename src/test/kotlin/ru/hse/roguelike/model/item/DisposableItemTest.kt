package ru.hse.roguelike.model.item

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.Hero
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


internal class DisposableItemTest {

    @Test
    fun `test disposable works`() {
        val item = DisposableItem("id", "Test", "Test", 4, 5)
        val hero = Hero(100, 100, Position(0, 0), mutableListOf(item))
        assertEquals(ItemType.Disposable, item.itemType)
        assertFalse(item.isUsed)
        assertTrue(item.canApply(hero))
        item.apply(hero)
        assertTrue(item.isUsed)
        assertEquals(104, hero.health)
        assertEquals(105, hero.maximumHealth)
    }

    @Test
    fun `test disposable can't be used again`() {
        val item = DisposableItem("id", "Test", "Test", 4, 5)
        val hero = Hero(100, 100, Position(0, 0), mutableListOf(item))
        item.apply(hero)
        assertFalse(item.canApply(hero))
    }

    @Test
    fun `test disposable can't be canceled`() {
        val item = DisposableItem("id", "Test", "Test", 4, 5)
        val hero = Hero(100, 100, Position(0, 0), mutableListOf(item))
        item.apply(hero)
        assertThrows<IllegalStateException> {
            item.cancel(hero)
        }
    }
}
