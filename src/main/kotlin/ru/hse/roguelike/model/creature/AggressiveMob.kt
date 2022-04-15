package ru.hse.roguelike.model.creature

import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.strategy.AggressiveStrategy

class AggressiveMob(
    health: Int,
    maximumHealth: Int,
    attackDamage: Int,
    position: Position,
    vision: Int
) : Mob(health, maximumHealth, attackDamage, position, AggressiveStrategy(vision))
