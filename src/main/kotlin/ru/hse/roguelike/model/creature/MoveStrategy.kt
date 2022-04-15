package ru.hse.roguelike.model.creature

import ru.hse.roguelike.model.GameField
import ru.hse.roguelike.model.Position

/**
 * Стратегия передвижения моба
 */
interface MoveStrategy {
    /**
     * @param gameField игровое поле, на котором находится моб
     * @param mob моб, которого нужно передвинуть
     *
     * @return позицию, в которую нужно переместить моба
     */
    fun move(gameField: GameField, mob: Mob): Position
}
