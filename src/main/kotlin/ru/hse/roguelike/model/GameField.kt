package ru.hse.roguelike.model

class GameField(
    private val field: List<List<Cell>>
) {
    val width = field.firstOrNull()?.size ?: 0
    val height = field.size

    fun get(x: Int, y: Int): Cell = field[y][x]
    fun get(position: Position): Cell = field[position.y][position.x]
}
