package ru.hse.roguelike.model.creature.mob

import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.mob.state.MobState
import ru.hse.roguelike.model.creature.mob.state.OrdinaryMobState
import ru.hse.roguelike.model.creature.strategy.CowardStrategy

class CowardMob(
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
    CowardStrategy(vision),
    MobType.Coward,
    description,
    passiveHeal
) {

    override fun clone(): Mob =
        CowardMob(health, maximumHealth, attackDamage, position, vision, description, state, passiveHeal)
}
