package ru.hse.roguelike.model.creature.mob

import ru.hse.roguelike.model.GameField
import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.BaseCreature
import ru.hse.roguelike.model.creature.Creature
import ru.hse.roguelike.model.creature.Hero
import ru.hse.roguelike.model.creature.strategy.MoveStrategy
import kotlin.math.min

/**
 * Базовый класс для мобов
 */
open class BaseMob protected constructor(
    health: Int,
    maximumHealth: Int,
    attackDamage: Int,
    override var position: Position,
    override var moveStrategy: MoveStrategy
) : Mob {

    override fun move(gameField: GameField): Mob {
        val newPosition = moveStrategy.move(gameField, this)
        if (newPosition == position) {
            return this
        }
        val oldCell = gameField.get(position)
        val newCell = gameField.get(newPosition)
        val newCellCreature = newCell.creature
        if (newCellCreature is Hero) {
            newCellCreature.health -= attackDamage
            health -= newCellCreature.attackDamage
        } else {
            oldCell.creature = null
            newCell.creature = this
            position = newPosition
        }
        return this
    }

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
