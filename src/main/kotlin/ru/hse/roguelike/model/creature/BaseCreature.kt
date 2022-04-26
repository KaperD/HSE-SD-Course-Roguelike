package ru.hse.roguelike.model.creature

import ru.hse.roguelike.model.Position
import kotlin.math.min

/**
 * Базовый класс для существ, проверяет корректность новых значений полей
 **/
open class BaseCreature protected constructor(
    health: Int,
    maximumHealth: Int,
    attackDamage: Int,
    override var position: Position
) : Creature {
    override var health: Int = health
        set(value) {
            field = min(maximumHealth, value)
        }

    override var maximumHealth: Int = maximumHealth
        set(value) {
            require(value > 0) { "Maximum health must be positive" }
            field = value
            health = min(health, value)
        }

    override var attackDamage: Int = attackDamage
        set(value) {
            require(value >= 0) { "Attack must be non negative" }
            field = value
        }
}
