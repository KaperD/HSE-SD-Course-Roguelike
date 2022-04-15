package ru.hse.roguelike.model.creature

import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.strategy.PassiveStrategy

class PassiveMob(
    health: Int,
    maximumHealth: Int,
    attackDamage: Int,
    position: Position
) : Mob(health, maximumHealth, attackDamage, position, PassiveStrategy())
