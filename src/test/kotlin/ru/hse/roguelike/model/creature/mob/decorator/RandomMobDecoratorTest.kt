package ru.hse.roguelike.model.creature.mob.decorator

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.hse.roguelike.model.Cell
import ru.hse.roguelike.model.GameField
import ru.hse.roguelike.model.GroundType
import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.mob.AggressiveMob
import ru.hse.roguelike.model.creature.mob.Mob
import ru.hse.roguelike.model.creature.mob.PassiveMob
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotEquals

internal class RandomMobDecoratorTest {

    @Test
    fun `test decorator changes behavior for given time limit`() {
        var position = Position(0, 0)
        var mobState: Mob = PassiveMob(100, 100, 10, position, "")
        val field = GameField(List(2) { List(2) { Cell(GroundType.Land, mutableListOf(), null) } })
        field.get(0, 0).creature = mobState

        mobState = mobState.move(field)
        assertIs<PassiveMob>(mobState)
        assertEquals(position, mobState.position)

        mobState = RandomMobDecorator(mobState, 2)
        mobState = mobState.move(field)
        assertIs<RandomMobDecorator>(mobState)
        assertNotEquals(Position(0, 0), mobState.position)

        position = mobState.position
        mobState = mobState.move(field)
        assertIs<PassiveMob>(mobState)
        assertNotEquals(position, mobState.position)

        position = mobState.position
        mobState = mobState.move(field)
        assertIs<PassiveMob>(mobState)
        assertEquals(position, mobState.position)
    }

    @Test
    fun `test decorator checks time limit`() {
        assertThrows<IllegalArgumentException> {
            RandomMobDecorator(AggressiveMob(100, 100, 0, Position(0, 0), 3, ""), -1)
        }
    }
}
