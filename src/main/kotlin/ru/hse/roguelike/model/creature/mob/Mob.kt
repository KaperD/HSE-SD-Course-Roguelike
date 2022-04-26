package ru.hse.roguelike.model.creature.mob

import ru.hse.roguelike.model.GameField
import ru.hse.roguelike.model.creature.Creature
import ru.hse.roguelike.model.creature.Hero
import ru.hse.roguelike.model.creature.strategy.MoveStrategy

/**
 * Игровой моб. Может иметь различные стратегии перемещения
 */
interface Mob : Creature {
    /**
     * Тип моба
     */
    val mobType: MobType

    /**
     * Стратегия перемещения
     */
    var moveStrategy: MoveStrategy

    /**
     * Делает 1 ход
     * @param gameField игровое поле, на котором находится моб
     *
     * @return новое состояние моба
     */
    fun move(gameField: GameField): Mob {
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
}

enum class MobType {
    Coward,
    Aggressive,
    Passive
}
