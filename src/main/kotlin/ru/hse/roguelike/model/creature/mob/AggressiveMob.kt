package ru.hse.roguelike.model.creature.mob

import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.mob.state.MobState
import ru.hse.roguelike.model.creature.mob.state.OrdinaryMobState
import ru.hse.roguelike.model.creature.strategy.AggressiveStrategy

class AggressiveMob(
    health: Int,
    maximumHealth: Int,
    attackDamage: Int,
    position: Position,
    private val vision: Int,
    description: String,
    state: MobState = OrdinaryMobState(0),
    passiveHeal: Int = 0
) : BaseMob(
    health,
    maximumHealth,
    attackDamage,
    position,
    state,
    AggressiveStrategy(vision),
    MobType.Aggressive,
    description,
    passiveHeal
) {

    override fun clone(): Mob =
        AggressiveMob(health, maximumHealth, attackDamage, position, vision, description, state, passiveHeal)
}
