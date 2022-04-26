package ru.hse.roguelike.model.creature.mob

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.hse.roguelike.model.Position

internal class BaseMobTest {

    @Test
    fun `test can't change maximum health and attack damage`() {
        val mob = PassiveMob(10, 10, 10, Position(0, 0))
        assertThrows<UnsupportedOperationException> { mob.maximumHealth = 20 }
        assertThrows<UnsupportedOperationException> { mob.attackDamage = 20 }
    }
}
