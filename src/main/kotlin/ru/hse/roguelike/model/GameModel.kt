package ru.hse.roguelike.model

import ru.hse.roguelike.model.creature.Hero
import ru.hse.roguelike.model.creature.mob.Mob

/**
 * Модель игры, описывающаяся текущим состоянием игрового поля и героем
 * @param field игровое поле
 * @param hero  герой
 */
class GameModel(
    var field: GameField,
    var mobs: List<Mob>,
    val hero: Hero
)
