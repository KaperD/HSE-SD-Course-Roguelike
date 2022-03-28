package ru.hse.roguelike.model.creature

import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.item.Item

class Hero(
    override var health: Int,
    override var maximumHealth: Int,
    override var position: Position,
    val items: MutableList<Item>
) : Creature
