package ru.hse.roguelike.factory

import ru.hse.roguelike.model.GameField
import ru.hse.roguelike.model.Position

interface GameFieldFactory {
    /**
     * @return game field and initial hero position
     */
    fun getByLevel(level: Int): Pair<GameField, Position>

    /**
     * @return game field and initial hero position
     */
    fun generate(): Pair<GameField, Position>
}
