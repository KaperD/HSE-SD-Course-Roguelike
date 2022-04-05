package ru.hse.roguelike.model.item

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.Hero
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class ReusableItemTest {

    @Test
    fun `test reusable works`() {
        val item = ReusableItem("id", "Test", "Test", ItemType.Head, 4, 5)
        val hero = Hero(100, 100, Position(0, 0), mutableListOf(item))
        assertEquals(ItemType.Head, item.itemType)
        assertFalse(item.isUsed)
        assertTrue(item.canApply(hero))
        item.apply(hero)
        assertTrue(item.isUsed)
        assertEquals(104, hero.health)
        assertEquals(105, hero.maximumHealth)
    }

    @Test
    fun `test reusable can't be used again`() {
        val item = ReusableItem("id", "Test", "Test", ItemType.Head, 4, 5)
        val hero = Hero(100, 100, Position(0, 0), mutableListOf(item))
        item.apply(hero)
        assertFalse(item.canApply(hero))
    }

    @Test
    fun `test reusable cancel`() {
        val item = ReusableItem("id", "Test", "Test", ItemType.Head, 4, 5)
        val hero = Hero(100, 100, Position(0, 0), mutableListOf(item))
        item.apply(hero)
        item.cancel(hero)
        assertEquals(100, hero.health)
        assertEquals(100, hero.maximumHealth)
    }

    @Test
    fun `test reusable with different type`() {
        val item1 = ReusableItem("id", "Test", "Test", ItemType.Head, 4, 5)
        val item2 = ReusableItem("id2", "Test2", "Test2", ItemType.Body, 4, 5)
        val hero = Hero(100, 100, Position(0, 0), mutableListOf(item1, item2))
        item1.apply(hero)
        assertTrue(item2.canApply(hero))
        item2.apply(hero)
        assertEquals(108, hero.health)
        assertEquals(110, hero.maximumHealth)
    }

    @Test
    fun `test reusable with same type`() {
        val item1 = ReusableItem("id", "Test", "Test", ItemType.Head, 4, 5)
        val item2 = ReusableItem("id2", "Test2", "Test2", ItemType.Head, 4, 5)
        val hero = Hero(100, 100, Position(0, 0), mutableListOf(item1, item2))
        item1.apply(hero)
        assertFalse(item2.canApply(hero))
    }

    @Test
    fun `test reusable wrong apply`() {
        val item1 = ReusableItem("id", "Test", "Test", ItemType.Head, 4, 5)
        val item2 = ReusableItem("id2", "Test2", "Test2", ItemType.Head, 4, 5)
        val hero = Hero(100, 100, Position(0, 0), mutableListOf(item1, item2))
        item1.apply(hero)
        assertThrows<IllegalArgumentException> {
            item2.apply(hero)
        }
    }
}
