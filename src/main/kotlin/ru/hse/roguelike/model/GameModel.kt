package ru.hse.roguelike.model

import ru.hse.roguelike.model.creature.Hero

/**
 * Модель игры, описывающаяся текущим состоянием игрового поля и героем
 * @param field игровое поле
 * @param hero  герой
 */
class GameModel(
    var field: GameField,
    val hero: Hero
)
