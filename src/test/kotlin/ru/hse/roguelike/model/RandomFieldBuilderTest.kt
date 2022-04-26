package ru.hse.roguelike.model

import org.junit.jupiter.api.Test
import ru.hse.roguelike.factory.item.ItemFactoryImpl
import ru.hse.roguelike.factory.mob.FantasyMobFactory
import ru.hse.roguelike.factory.mob.SwampMobFactory
import java.util.LinkedList
import java.util.Queue
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class RandomFieldBuilderTest {

    @Test
    fun `test correct dimensions`() {
        val (field, mobs, hero) = GameField.builder().generateRandom()
            .withWidth(200)
            .withHeight(300)
            .withItemFactory(ItemFactoryImpl())
            .withMobFactory(SwampMobFactory())
            .withNumberOfItems(10)
            .withNumberOfMobs(10)
            .build()
        assertEquals(200, field.width)
        assertEquals(300, field.height)
    }

    @Test
    fun `test correct mobs`() {
        val (field, mobs, hero) = GameField.builder().generateRandom()
            .withWidth(20)
            .withHeight(30)
            .withItemFactory(ItemFactoryImpl())
            .withMobFactory(FantasyMobFactory())
            .withNumberOfItems(0)
            .withNumberOfMobs(0)
            .build()
        mobs.forEach {
            assertEquals(field.get(it.position).creature, it)
        }
    }

    @Test
    fun `test hero can finish level`() {
        val (field, mobs, hero) = GameField.builder().generateRandom()
            .withWidth(20)
            .withHeight(30)
            .withItemFactory(ItemFactoryImpl())
            .withMobFactory(FantasyMobFactory())
            .withNumberOfItems(0)
            .withNumberOfMobs(0)
            .build()

        val visited = mutableSetOf(Position(hero.x, hero.y))
        val queue: Queue<Position> = LinkedList(listOf(Position(hero.x, hero.y)))
        var hasPath = false
        bfs@ while (!queue.isEmpty()) {
            val position = queue.remove()
            val potentialPositions = listOf(
                position.copy(x = position.x + 1),
                position.copy(x = position.x - 1),
                position.copy(y = position.y + 1),
                position.copy(y = position.y - 1)
            )
            for (nextPosition in potentialPositions) {
                if (nextPosition.x in 0 until field.width
                    && nextPosition.y in 0 until field.height) {
                    val cell = field.get(nextPosition)
                    if (cell.groundType == GroundType.LevelEnd) {
                        hasPath = true
                        break@bfs
                    } else if (cell.groundType.isPassable && nextPosition !in visited) {
                        visited.add(nextPosition)
                        queue.add(nextPosition)
                    }
                }
            }
        }
        assertTrue(hasPath)
    }
}
