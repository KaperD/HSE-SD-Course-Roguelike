package ru.hse.roguelike.model.creature.strategy

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import ru.hse.roguelike.model.Cell
import ru.hse.roguelike.model.GameField
import ru.hse.roguelike.model.GroundType
import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.Hero
import ru.hse.roguelike.model.creature.mob.CowardMob
import kotlin.test.assertEquals


internal class CowardStrategyTest {
    @Test
    fun `test hero is too far`() {
        val gameField = GameField(createEmptyGameField(5))
        val cowardStrategy = CowardStrategy(2)
        val mob = CowardMob(100, 100, 1, Position(0, 0), 2)
        val hero = Hero(100, 100, 1, Position(4, 4), mutableListOf())
        gameField.get(mob.position).creature = mob
        gameField.get(hero.position).creature = hero
        assertEquals(Position(0, 0), cowardStrategy.move(gameField, mob))
        assertEquals(Position(0, 0), mob.position)
        assertEquals(mob, gameField.get(0, 0).creature)
    }

    @Test
    fun `test step away from hero`() {
        val gameField = GameField(createEmptyGameField(10))
        val cowardStrategy = CowardStrategy(5)
        val mob = CowardMob(100, 100, 1, Position(4, 4), 5)
        val hero = Hero(100, 100, 1, Position(5, 7), mutableListOf())
        gameField.get(mob.position).creature = mob
        gameField.get(hero.position).creature = hero
        val strategyResult = cowardStrategy.move(gameField, mob)
        assertTrue(Position(3, 4) == strategyResult || Position(4, 3) == strategyResult)
    }

    @Test
    fun  `test cannot see hero`() {
        val gameFieldList = createEmptyGameField(10)
        val cowardStrategy = CowardStrategy(5)
        val mob = CowardMob(100, 100, 1, Position(4, 4), 5)
        val hero = Hero(100, 100, 1, Position(5, 7), mutableListOf())
        gameFieldList[3][4] = Cell(GroundType.Stone, mutableListOf(), null)
        gameFieldList[5][4] = Cell(GroundType.Stone, mutableListOf(), null)
        gameFieldList[4][3] = Cell(GroundType.Stone, mutableListOf(), null)
        gameFieldList[4][4] = Cell(GroundType.Stone, mutableListOf(), null)
        val gameField = GameField(gameFieldList)
        gameField.get(mob.position).creature = mob
        gameField.get(hero.position).creature = hero
        val strategyResult = cowardStrategy.move(gameField, mob)
        assertEquals(Position(4, 4), strategyResult)
    }

    @Test
    fun  `test better to stay `() {
        val gameFieldList = createEmptyGameField(10)
        val cowardStrategy = CowardStrategy(6)
        val mob = CowardMob(100, 100, 1, Position(2, 2), 6)
        val hero = Hero(100, 100, 1, Position(4, 2), mutableListOf())
        gameFieldList[2][1] = Cell(GroundType.Water, mutableListOf(), null)
        gameFieldList[1][2] = Cell(GroundType.Water, mutableListOf(), null)
        gameFieldList[3][2] = Cell(GroundType.Water, mutableListOf(), null)
        val gameField = GameField(gameFieldList)
        gameField.get(mob.position).creature = mob
        gameField.get(hero.position).creature = hero
        val strategyResult = cowardStrategy.move(gameField, mob)
        assertEquals(Position(2, 2), strategyResult)
    }

    private fun createEmptyGameField(n: Int): MutableList<MutableList<Cell>> {
        return MutableList(n) { MutableList(n) { Cell(GroundType.Land, mutableListOf(), null) } }
    }
}
