package ru.hse.roguelike.model.creature.mob

import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.strategy.CowardStrategy

class CowardMob(
    health: Int,
    maximumHealth: Int,
    attackDamage: Int,
    position: Position,
    vision: Int
) : BaseMob(health, maximumHealth, attackDamage, position, CowardStrategy(vision), MobType.Coward)
