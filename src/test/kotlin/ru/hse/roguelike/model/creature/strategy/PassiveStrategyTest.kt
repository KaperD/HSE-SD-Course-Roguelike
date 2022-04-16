package ru.hse.roguelike.model.creature.strategy

import org.junit.jupiter.api.Test
import ru.hse.roguelike.model.Cell
import ru.hse.roguelike.model.GameField
import ru.hse.roguelike.model.GroundType
import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.mob.PassiveMob
import kotlin.test.assertEquals

internal class PassiveStrategyTest {
    @Test
    fun `test move`() {
        val width = 5
        val height = 5
        val gameField = GameField(List(height) { List(width) { Cell(GroundType.Land, mutableListOf(), null) } })
        val strategy = PassiveStrategy()
        val mob = PassiveMob(100, 100, 0, Position(2, 2))
        gameField.get(2, 2).creature = mob
        assertEquals(Position(2, 2), strategy.move(gameField, mob))
        assertEquals(mob, gameField.get(2, 2).creature)
        assertEquals(Position(2, 2), mob.position)
    }
}
