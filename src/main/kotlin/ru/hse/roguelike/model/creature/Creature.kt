package ru.hse.roguelike.model.creature

import ru.hse.roguelike.model.Position
import kotlin.math.min

/**
* Существо находящееся на карте и имеющее здоровье
 * @property health        текущее здоровье
 * @property maximumHealth ограничение на здоровье сверху
 * @property attackDamage  урон, который наносит существо за один удар
 * @property position      позиция на карте
**/
open class Creature protected constructor(
    health: Int,
    maximumHealth: Int,
    attackDamage: Int,
    var position: Position
) {
    var health: Int = health
        set(value) {
            field = min(maximumHealth, value)
        }

    var maximumHealth: Int = maximumHealth
        set(value) {
            require(value > 0) { "Maximum health must be positive" }
            field = value
            health = min(health, value)
        }

    var attackDamage: Int = attackDamage
        set(value) {
            require(value >= 0) { "Attack must be non negative" }
            field = value
        }
}
