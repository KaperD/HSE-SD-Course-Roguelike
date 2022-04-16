package ru.hse.roguelike.model.creature.mob

import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.strategy.AggressiveStrategy

class AggressiveMob(
    health: Int,
    maximumHealth: Int,
    attackDamage: Int,
    position: Position,
    vision: Int
) : BaseMob(health, maximumHealth, attackDamage, position, AggressiveStrategy(vision), MobType.Aggressive)
