package ru.hse.roguelike.model

data class Cell(
    val groundType: GroundType,
    val items: MutableList<Item>,
    var creature: Creature?
)

enum class GroundType {
    Land,
    Water,
    Fire,
    Stone,
    LevelEnd
}
