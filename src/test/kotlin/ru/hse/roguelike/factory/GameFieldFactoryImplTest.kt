package ru.hse.roguelike.factory

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.hse.roguelike.model.GroundType
import ru.hse.roguelike.model.Position
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class GameFieldFactoryImplTest {

    @Test
    fun `test level matches file`() {
        val width = 3
        val height = 2
        val factory = GameFieldFactoryImpl(width, height, ItemFactoryImpl())
        val (field, heroPosition) = factory.getByLevelName("test")

        assertEquals(width, field.width)
        assertEquals(height, field.height)

        for (y in 0 until height) {
            for (x in 0 until width) {
                assertNull(field.get(x, y).creature)
            }
        }

        assertEquals(GroundType.Land, field.get(0, 0).groundType)
        assertEquals(GroundType.Land, field.get(1, 0).groundType)
        assertEquals(GroundType.Water, field.get(2, 0).groundType)
        assertEquals(GroundType.Fire, field.get(0, 1).groundType)
        assertEquals(GroundType.Stone, field.get(1, 1).groundType)
        assertEquals(GroundType.LevelEnd, field.get(2, 1).groundType)
        assertTrue(field.get(0, 0).items.isNotEmpty())
        assertEquals("healing_salve", field.get(0, 1).items.first().id)

        assertEquals(Position(1, 0), heroPosition)
    }

    @Test
    fun `test factory checks dimensions`() {
        val factory = GameFieldFactoryImpl(2, 2, ItemFactoryImpl())
        assertThrows<IllegalArgumentException> {
            factory.getByLevelName("test")
        }
    }

    @Test
    fun `test wrong level name`() {
        val factory = GameFieldFactoryImpl(3, 2, ItemFactoryImpl())
        assertThrows<IllegalStateException> {
            factory.getByLevelName("wrongName")
        }
    }

    @Test
    fun `test no hero`() {
        val factory = GameFieldFactoryImpl(3, 2, ItemFactoryImpl())
        assertThrows<IllegalArgumentException> {
            factory.getByLevelName("no_hero")
        }
    }

    @Test
    fun `test wrong char`() {
        val factory = GameFieldFactoryImpl(3, 2, ItemFactoryImpl())
        assertThrows<IllegalStateException> {
            factory.getByLevelName("wrong_char")
        }
    }

    @Test
    fun `test no items`() {
        val width = 3
        val height = 2
        val factory = GameFieldFactoryImpl(width, height, ItemFactoryImpl())
        val (field, _) = factory.getByLevelName("no_items")

        for (y in 0 until height) {
            for (x in 0 until width) {
                assertTrue(field.get(x, y).items.isEmpty())
            }
        }
    }

    @Test
    fun generate() {
        val factory = GameFieldFactoryImpl(3, 2, ItemFactoryImpl())
        assertThrows<NotImplementedError> {
            factory.generate()
        }
    }
}
