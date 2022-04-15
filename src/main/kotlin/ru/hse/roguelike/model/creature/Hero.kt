package ru.hse.roguelike.model.creature

import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.item.Item

/**
 * Главный персонаж, от лица которого игрок ведёт игру
 * @property items текущий набор предметов имеющихся у героя
 */
class Hero(
    health: Int,
    maximumHealth: Int,
    attackDamage: Int,
    position: Position,
    val items: MutableList<Item>,
    var level: Int = 1,
    var experienceForNextLevel: Int = 100
) : Creature(health, maximumHealth, attackDamage, position)
