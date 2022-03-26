package ru.hse.roguelike.model

class Cell(
    val groundType: GroundType,
    val items: MutableList<Item>,
    val creature: Creature?
)

enum class GroundType {
    Land,
    Water,
    Fire,
    Stone,
    LevelEnd
}
