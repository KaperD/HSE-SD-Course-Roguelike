package ru.hse.roguelike.model.creature

import ru.hse.roguelike.model.Position

/**
 * Существо находящееся на карте и имеющее здоровье
 **/
interface Creature {

    /**
     * Текущее здоровье
     */
    var health: Int

    /**
     * Ограничение на здоровье сверху
     */
    var maximumHealth: Int

    /**
     * Урон, который наносит существо за один удар
     */
    var attackDamage: Int

    /**
     * Позиция на карте
     */
    var position: Position
}
