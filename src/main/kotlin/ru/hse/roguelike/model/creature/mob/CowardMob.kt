package ru.hse.roguelike.model.creature.mob

import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.strategy.CowardStrategy

class CowardMob(
    health: Int,
    maximumHealth: Int,
    attackDamage: Int,
    position: Position,
    private val vision: Int,
    description: String
) : BaseMob(health, maximumHealth, attackDamage, position, CowardStrategy(vision), MobType.Coward, description) {

    override fun clone(): Mob = CowardMob(health, maximumHealth, attackDamage, position, vision, description)
}
