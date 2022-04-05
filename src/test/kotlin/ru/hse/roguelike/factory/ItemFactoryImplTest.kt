package ru.hse.roguelike.factory

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.Hero
import ru.hse.roguelike.model.item.Item
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class ItemFactoryImplTest {

    @Test
    fun `test get by id returns copies`() {
        val factory = ItemFactoryImpl()
        val hero = Hero(20, 100, Position(1, 1), mutableListOf())
        val id = factory.getRandom().id
        val first = factory.getById(id)
        val second = factory.getById(id)
        hero.items.add(first)
        first.apply(hero)
        assertTrue(first.isUsed)
        assertFalse(second.isUsed)
    }

    @Test
    fun `test get by whong id`() {
        val factory = ItemFactoryImpl()
        assertThrows<IllegalStateException> {
            factory.getById("wrong_id")
        }
    }

    @Test
    fun `test get random returns copies`() {
        val factory = ItemFactoryImpl()
        val hero = Hero(20, 100, Position(1, 1), mutableListOf())
        val first = factory.getRandom()
        val second: Item = run {
            var item = factory.getRandom()
            while (item.id != first.id) {
                item = factory.getRandom()
            }
            item
        }
        hero.items.add(first)
        first.apply(hero)
        assertTrue(first.isUsed)
        assertFalse(second.isUsed)
    }
}
