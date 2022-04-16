package ru.hse.roguelike.factory

import ru.hse.roguelike.model.GameField
import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.Mob

/**
 * Фабрика, отвечающая за создание игрового поля
 */
interface GameFieldFactory {
    /**
     * Получение уровня по названию
     * @param name название уровня
     * @return игровое поле и начальная позиция игрока
     */
    fun getByLevelName(name: String): Triple<GameField, List<Mob>, Position>

    /**
     * Генерирование уровня
     * @return игровое поле и начальная позиция игрока
     */
    fun generate(): Triple<GameField, List<Mob>, Position>
}
