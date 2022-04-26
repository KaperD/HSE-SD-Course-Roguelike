package ru.hse.roguelike.model.creature.mob

import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.strategy.PassiveStrategy

class PassiveMob(
    health: Int,
    maximumHealth: Int,
    attackDamage: Int,
    position: Position
) : BaseMob(health, maximumHealth, attackDamage, position, PassiveStrategy(), MobType.Passive)
