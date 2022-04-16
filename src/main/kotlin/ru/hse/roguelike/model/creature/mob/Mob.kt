package ru.hse.roguelike.model.creature.mob

import ru.hse.roguelike.model.GameField
import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.BaseCreature
import ru.hse.roguelike.model.creature.Creature
import ru.hse.roguelike.model.creature.Hero
import ru.hse.roguelike.model.creature.strategy.MoveStrategy

/**
 * Игровой моб. Может иметь различные стратегии перемещения
 */
interface Mob : Creature {

    var moveStrategy: MoveStrategy
    /**
     * Делает 1 ход
     * @param gameField игровое поле, на котором находится моб
     *
     * @return новое состояние моба
     */
    fun move(gameField: GameField): Mob
}
