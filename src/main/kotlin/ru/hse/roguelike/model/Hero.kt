package ru.hse.roguelike.model

class Hero(
    override var health: Int,
    override var position: Position,
    val items: MutableList<Item>
) : Creature {
    override val info: String
        get() = TODO("Not yet implemented")
}
