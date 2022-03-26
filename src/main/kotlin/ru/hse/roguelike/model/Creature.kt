package ru.hse.roguelike.model

interface Creature {
    var health: Int
    var position: Position
}

data class Position(
    val x: Int,
    val y: Int
)
