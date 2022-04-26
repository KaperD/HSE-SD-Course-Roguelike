package ru.hse.roguelike.model.creature.strategy

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import ru.hse.roguelike.model.Cell
import ru.hse.roguelike.model.GameField
import ru.hse.roguelike.model.GroundType
import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.Hero
import ru.hse.roguelike.model.creature.mob.AggressiveMob
import kotlin.test.assertEquals

internal class AggressiveStrategyTest {
    @Test
    fun `test step towards hero`() {
        val gameField = GameField(createEmptyGameField(5))
        val aggressiveStrategy = AggressiveStrategy(10)
        val mob = AggressiveMob(100, 100, 1, Position(1, 1), 10)
        val hero = Hero(100, 100, 1, Position(4, 3), mutableListOf())
        gameField.get(mob.position).creature = mob
        gameField.get(hero.position).creature = hero
        val strategyResult = aggressiveStrategy.move(gameField, mob)
        assertTrue(Position(1, 2) == strategyResult || Position(2, 1) == strategyResult)
        assertEquals(Position(1, 1), mob.position)
        assertEquals(mob, gameField.get(1, 1).creature)
    }

    @Test
    fun `test hero is too far`() {
        val gameField = GameField(createEmptyGameField(6))
        val aggressiveStrategy = AggressiveStrategy(1)
        val mob = AggressiveMob(100, 100, 1, Position(1, 2), 1)
        val hero = Hero(100, 100, 1, Position(3, 2), mutableListOf())
        gameField.get(mob.position).creature = mob
        gameField.get(hero.position).creature = hero
        assertEquals(Position(1, 2), aggressiveStrategy.move(gameField, mob))
    }

    @Test
    fun `test cannot see hero`() {
        val gameFieldList = createEmptyGameField(7)
        val aggressiveStrategy = AggressiveStrategy(10)
        val mob = AggressiveMob(100, 100, 1, Position(0, 0), 10)
        val hero = Hero(100, 100, 1, Position(6, 0), mutableListOf())
        gameFieldList[1][0] = Cell(GroundType.Stone, mutableListOf(), null)
        gameFieldList[0][1] = Cell(GroundType.Stone, mutableListOf(), null)
        gameFieldList[1][1] = Cell(GroundType.Stone, mutableListOf(), null)
        val gameField = GameField(gameFieldList)
        gameField.get(mob.position).creature = mob
        gameField.get(hero.position).creature = hero
        assertEquals(Position(0, 0), aggressiveStrategy.move(gameField, mob))
    }

    @Test
    fun `test cannot reach hero`() {
        val gameFieldList = createEmptyGameField(4)
        val aggressiveStrategy = AggressiveStrategy(2)
        val mob = AggressiveMob(100, 100, 1, Position(3, 3), 2)
        val hero = Hero(100, 100, 1, Position(2, 2), mutableListOf())
        gameFieldList[3][2] = Cell(GroundType.Water, mutableListOf(), null)
        gameFieldList[2][3] = Cell(GroundType.Water, mutableListOf(), null)
        val gameField = GameField(gameFieldList)
        gameField.get(mob.position).creature = mob
        gameField.get(hero.position).creature = hero
        assertEquals(Position(3, 3), aggressiveStrategy.move(gameField, mob))
    }

    @Test
    fun `test mob on map 1x1`() {
        val gameField = GameField(createEmptyGameField(1))
        val aggressiveStrategy = AggressiveStrategy(10)
        val mob = AggressiveMob(100, 100, 1, Position(0, 0), 10)
        gameField.get(mob.position).creature = mob
        assertEquals(Position(0, 0), aggressiveStrategy.move(gameField, mob))
    }

    private fun createEmptyGameField(n: Int): MutableList<MutableList<Cell>> {
        return MutableList(n) { MutableList(n) { Cell(GroundType.Land, mutableListOf(), null) } }
    }
}
