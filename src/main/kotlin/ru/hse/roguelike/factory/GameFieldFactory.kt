package ru.hse.roguelike.factory

import ru.hse.roguelike.model.GameField

interface GameFieldFactory {
    fun getByLevel(level: Int): GameField
    fun generate(): GameField
}
