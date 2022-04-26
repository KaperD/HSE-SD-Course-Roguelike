package ru.hse.roguelike.model.creature.mob.state

import org.junit.jupiter.api.Test
import ru.hse.roguelike.model.Cell
import ru.hse.roguelike.model.GameField
import ru.hse.roguelike.model.GroundType
import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.mob.PassiveMob
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

internal class PanicMobStateTest {

    @Test
    fun `test panic state changes strategy`() {
        val mob = PassiveMob(100, 100, 1, Position(1, 1), "", OrdinaryMobState(50))

        val field = GameField(
            List(3) {
                List(3) {
                    Cell(GroundType.Land, mutableListOf(), null)
                }
            }
        )
        field.get(mob.position).creature = mob

        mob.move(field)
        assertEquals(Position(1, 1), mob.position)

        var pos = mob.position
        mob.health = 40
        mob.move(field)
        assertNotEquals(pos, mob.position)

        pos = mob.position
        mob.move(field)
        assertNotEquals(pos, mob.position)

        pos = mob.position
        mob.health = 50
        mob.move(field)
        assertEquals(pos, mob.position)
    }
}
