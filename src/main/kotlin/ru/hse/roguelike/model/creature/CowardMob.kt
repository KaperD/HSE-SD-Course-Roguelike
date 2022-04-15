package ru.hse.roguelike.model.creature

import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.strategy.CowardStrategy

class CowardMob(
    health: Int,
    maximumHealth: Int,
    attackDamage: Int,
    position: Position,
    vision: Int
) : Mob(health, maximumHealth, attackDamage, position, CowardStrategy(vision))
