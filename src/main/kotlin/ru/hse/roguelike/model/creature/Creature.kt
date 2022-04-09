package ru.hse.roguelike.model.creature

import ru.hse.roguelike.model.Position

/**
* Существо находящееся на карте и имеющее здоровье
 * @property health        текущее здоровье
 * @property maximumHealth ограничение на здоровье сверху
 * @property position      позиция на карте
**/
interface Creature {
    var health: Int
    var maximumHealth: Int
    var position: Position
}
