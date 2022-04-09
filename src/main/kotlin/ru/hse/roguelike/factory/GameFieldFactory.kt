package ru.hse.roguelike.factory

import ru.hse.roguelike.model.GameField
import ru.hse.roguelike.model.Position

/**
 * Фабрика, отвечающая за создание игрового поля
 */
interface GameFieldFactory {
    /**
     * Получение уровня по названию
     * @param name название уровня
     * @return игровое поле и начальная позиция игрока
     */
    fun getByLevelName(name: String): Pair<GameField, Position>

    /**
     * Генерирование уровня
     * @return игровое поле и начальная позиция игрока
     */
    fun generate(): Pair<GameField, Position>
}
