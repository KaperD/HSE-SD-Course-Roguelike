package ru.hse.roguelike.model.creature

import ru.hse.roguelike.model.Position

interface Creature {
    var health: Int
    var maximumHealth: Int
    var position: Position
}
