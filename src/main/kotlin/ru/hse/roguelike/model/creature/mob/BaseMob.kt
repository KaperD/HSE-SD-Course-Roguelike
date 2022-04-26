package ru.hse.roguelike.model.creature.mob

import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.strategy.MoveStrategy
import kotlin.math.min

/**
 * Базовый класс для мобов
 */
abstract class BaseMob(
    health: Int,
    maximumHealth: Int,
    attackDamage: Int,
    override var position: Position,
    override var moveStrategy: MoveStrategy,
    override val mobType: MobType,
    override val description: String
) : Mob {

    override var health: Int = health
        set(value) {
            field = min(maximumHealth, value)
        }

    override var maximumHealth: Int = maximumHealth
        set(_) = throw UnsupportedOperationException()

    override var attackDamage: Int = attackDamage
        set(_) = throw UnsupportedOperationException()
}
