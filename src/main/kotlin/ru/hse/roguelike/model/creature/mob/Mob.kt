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
     * Описание моба
     */
    val description: String

    /**
     * Делает 1 ход
     * @param gameField игровое поле, на котором находится моб
     *
     * @return новое состояние моба и, возможно, новые мобы
     */
    fun move(gameField: GameField): List<Mob> {
        val newPosition = moveStrategy.move(gameField, this)
        if (newPosition == position) {
            return listOf(this)
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
        return listOf(this)
    }

    /**
     * Создает копию моба
     */
    fun clone(): Mob
}

enum class MobType {
    Coward,
    Aggressive,
    Passive
}
