package ru.hse.roguelike.model.creature.mob

import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.strategy.PassiveStrategy

class PassiveMob(
    health: Int,
    maximumHealth: Int,
    attackDamage: Int,
    position: Position,
    description: String
) : BaseMob(health, maximumHealth, attackDamage, position, PassiveStrategy(), MobType.Passive, description) {

    override fun clone(): Mob = PassiveMob(health, maximumHealth, attackDamage, position, description)
}
