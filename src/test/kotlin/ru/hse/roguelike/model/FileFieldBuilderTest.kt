package ru.hse.roguelike.model

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.hse.roguelike.factory.item.ItemFactoryImpl
import ru.hse.roguelike.factory.mob.FantasyMobFactory
import ru.hse.roguelike.factory.mob.MobFactory
import ru.hse.roguelike.model.creature.mob.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class FileFieldBuilderTest {

    @Test
    fun `test level matches file`() {
        val width = 3
        val height = 2
        val (field, mobs, heroPosition) = GameField.builder().loadFromFile("test")
            .withWidth(width)
            .withHeight(height)
            .withItemFactory(ItemFactoryImpl())
            .withMobFactory(object : MobFactory {
                override fun createCoward(position: Position): Mob {
                    return CowardMob(100, 100, 10, position, 4, "")
                }

                override fun createAggressive(position: Position): Mob {
                    return AggressiveMob(100, 100, 10, position, 4, "")
                }

                override fun createPassive(position: Position): Mob {
                    return PassiveMob(100, 100, 10, position,  "")
                }
            })
            .build()

        assertEquals(width, field.width)
        assertEquals(height, field.height)

        assertEquals(listOf(MobType.Coward, MobType.Passive, MobType.Aggressive), mobs.map { it.mobType })
        assertTrue { mobs.all { it.health == 100 && it.maximumHealth == 100 } }

        assertEquals(GroundType.Land, field.get(0, 0).groundType)
        assertEquals(GroundType.Land, field.get(1, 0).groundType)
        assertEquals(GroundType.Water, field.get(2, 0).groundType)
        assertEquals(GroundType.Fire, field.get(0, 1).groundType)
        assertEquals(GroundType.Stone, field.get(1, 1).groundType)
        assertEquals(GroundType.LevelEnd, field.get(2, 1).groundType)
        assertTrue(field.get(0, 0).items.isNotEmpty())
        assertEquals("healing_salve", field.get(0, 1).items.first().id)
        assertEquals(null, field.get(0, 0).creature as? Mob)
        assertEquals(MobType.Coward, (field.get(1, 0).creature as Mob).mobType)
        assertEquals(null, field.get(2, 0).creature as? Mob)
        assertEquals(MobType.Aggressive, (field.get(0, 1).creature as Mob).mobType)
        assertEquals(null, field.get(1, 1).creature as? Mob)
        assertEquals(MobType.Passive, (field.get(2, 1).creature as Mob).mobType)

        assertEquals(Position(1, 0), heroPosition)
    }

    @Test
    fun `test builder checks dimensions`() {
        assertThrows<IllegalArgumentException> {
            GameField.builder().loadFromFile("test")
                .withWidth(2)
                .withHeight(2)
                .withItemFactory(ItemFactoryImpl())
                .withMobFactory(FantasyMobFactory())
                .build()
        }
    }

    @Test
    fun `test wrong level name`() {
        assertThrows<IllegalStateException> {
            GameField.builder().loadFromFile("wrongName")
                .withWidth(3)
                .withHeight(2)
                .withItemFactory(ItemFactoryImpl())
                .withMobFactory(FantasyMobFactory())
                .build()
        }
    }

    @Test
    fun `test no hero`() {
        assertThrows<IllegalArgumentException> {
            GameField.builder().loadFromFile("no_hero")
                .withWidth(3)
                .withHeight(2)
                .withItemFactory(ItemFactoryImpl())
                .withMobFactory(FantasyMobFactory())
                .build()
        }
    }

    @Test
    fun `test wrong char`() {
        assertThrows<IllegalStateException> {
            GameField.builder().loadFromFile("wrong_char")
                .withWidth(3)
                .withHeight(2)
                .withItemFactory(ItemFactoryImpl())
                .withMobFactory(FantasyMobFactory())
                .build()
        }
    }

    @Test
    fun `test no items`() {
        val width = 3
        val height = 2
        val (field, _) = GameField.builder().loadFromFile("no_items")
            .withWidth(3)
            .withHeight(2)
            .withItemFactory(ItemFactoryImpl())
            .withMobFactory(FantasyMobFactory())
            .build()


        for (y in 0 until height) {
            for (x in 0 until width) {
                assertTrue(field.get(x, y).items.isEmpty())
            }
        }
    }
}
