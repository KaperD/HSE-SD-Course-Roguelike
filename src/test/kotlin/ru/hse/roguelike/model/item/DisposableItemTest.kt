package ru.hse.roguelike.model.item

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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
        val hero = Hero(100, 100, 10, Position(0, 0), mutableListOf(item))
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
        val hero = Hero(100, 100, 10, Position(0, 0), mutableListOf(item))
        item.apply(hero)
        assertFalse(item.canApply(hero))
    }

    @Test
    fun `test disposable can't be canceled`() {
        val item = DisposableItem("id", "Test", "Test", 4, 5)
        val hero = Hero(100, 100, 10, Position(0, 0), mutableListOf(item))
        item.apply(hero)
        assertThrows<IllegalStateException> {
            item.cancel(hero)
        }
    }

    @Test
    fun `test disposable serialize`() {
        val item = DisposableItem("id", "Test", "Test", 4, 5)
        val json = Json {prettyPrint = true}
        assertEquals("""
            {
                "type": "Disposable",
                "id": "id",
                "name": "Test",
                "description": "Test",
                "healthChange": 4,
                "maximumHealthChange": 5
            }
        """.trimIndent(), json.encodeToString(item as Item))
        assertEquals("""
            {
                "id": "id",
                "name": "Test",
                "description": "Test",
                "healthChange": 4,
                "maximumHealthChange": 5
            }
        """.trimIndent(), json.encodeToString(item))

        val item2 = json.decodeFromString<Item>("""
            {
                "type": "Disposable",
                "id": "id",
                "name": "Test",
                "description": "Test",
                "healthChange": 4,
                "maximumHealthChange": 5
            }
        """.trimIndent())
        val item3 = json.decodeFromString<DisposableItem>("""
            {
                "id": "id",
                "name": "Test",
                "description": "Test",
                "healthChange": 4,
                "maximumHealthChange": 5
            }
        """.trimIndent())
        assertEquals(item, item2)
        assertEquals(item, item3)
    }
}
