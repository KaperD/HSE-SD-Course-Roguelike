package ru.hse.roguelike.model.creature.mob

import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.mob.state.MobState
import ru.hse.roguelike.model.creature.mob.state.OrdinaryMobState
import ru.hse.roguelike.model.creature.strategy.PassiveStrategy

class PassiveMob(
    health: Int,
    maximumHealth: Int,
    attackDamage: Int,
    position: Position,
    description: String,
    state: MobState = OrdinaryMobState(0),
    passiveHeal: Int = 0
) : BaseMob(
    health,
    maximumHealth,
    attackDamage,
    position,
    state,
    PassiveStrategy(),
    MobType.Passive,
    description,
    passiveHeal
) {

    override fun clone(): Mob =
        PassiveMob(health, maximumHealth, attackDamage, position, description, state, passiveHeal)
}
