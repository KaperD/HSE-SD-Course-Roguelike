package ru.hse.roguelike.model.creature.mob

import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.strategy.AggressiveStrategy

class AggressiveMob(
    health: Int,
    maximumHealth: Int,
    attackDamage: Int,
    position: Position,
    private val vision: Int,
    description: String
) : BaseMob(
    health,
    maximumHealth,
    attackDamage,
    position,
    AggressiveStrategy(vision),
    MobType.Aggressive,
    description
) {

    override fun clone(): Mob = AggressiveMob(health, maximumHealth, attackDamage, position, vision, description)
}
