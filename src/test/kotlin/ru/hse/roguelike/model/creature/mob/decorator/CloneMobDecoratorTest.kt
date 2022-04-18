package ru.hse.roguelike.model.creature.mob.decorator

import org.junit.jupiter.api.Test
import ru.hse.roguelike.model.Cell
import ru.hse.roguelike.model.GameField
import ru.hse.roguelike.model.GroundType
import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.mob.AggressiveMob
import kotlin.test.assertEquals

internal class CloneMobDecoratorTest {

    @Test
    fun `test mob is cloning`() {
        val field = GameField(
            List(3) { List(3) { Cell(GroundType.Land, mutableListOf(), null) } }
        )
        val mob = CloneMobDecorator(AggressiveMob(100, 100, 10, Position(1, 1), 4, ""), 100)
        field.get(1, 1).creature = mob
        repeat(4) {
            val mobs = mob.move(field).apply {
                forEach {
                    field.get(it.position).creature = it
                }
            }
            assertEquals(2, mobs.size)
        }
        var numberOfMobs = 0
        for (x in 0 until 3) {
            for (y in 0 until 3) {
                if (field.get(x, y).creature != null) {
                    numberOfMobs++
                }
            }
        }
        assertEquals(5, numberOfMobs)
    }

    @Test
    fun `test mob is not cloning when there is no space`() {
        val field = List(3) { MutableList(3) { Cell(GroundType.Stone, mutableListOf(), null) } }
        val mob = CloneMobDecorator(AggressiveMob(100, 100, 10, Position(1, 1), 4, ""), 100)
        field[1][1] = Cell(GroundType.Land, mutableListOf(), mob)
        val gameField = GameField(
            field
        )
        repeat(4) {
            val mobs = mob.move(gameField).apply {
                forEach {
                    gameField.get(it.position).creature = it
                }
            }
            assertEquals(1, mobs.size)
        }
        var numberOfMobs = 0
        for (x in 0 until 3) {
            for (y in 0 until 3) {
                if (gameField.get(x, y).creature != null) {
                    numberOfMobs++
                }
            }
        }
        assertEquals(1, numberOfMobs)
    }
}
