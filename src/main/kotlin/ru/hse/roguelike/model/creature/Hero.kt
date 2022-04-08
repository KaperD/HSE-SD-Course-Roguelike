package ru.hse.roguelike.model.creature

import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.item.Item
import kotlin.math.min

/**
 * Главный персонаж, от лица которого игрок ведёт игру
 * @property items текущий набор предметов имеющихся у героя
 */
class Hero(
    health: Int,
    maximumHealth: Int,
    override var position: Position,
    val items: MutableList<Item>
) : Creature {
    override var health: Int = health
        set(value) {
            field = min(maximumHealth, value)
        }

    override var maximumHealth: Int = maximumHealth
        set(value) {
            field = value
            health = min(health, value)
        }
}
