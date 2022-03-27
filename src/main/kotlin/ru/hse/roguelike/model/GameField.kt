package ru.hse.roguelike.model

class GameField(
    private val field: List<List<Cell>>
) {
    fun get(x: Int, y: Int): Cell = field[y][x]
    fun get(position: Position): Cell = field[position.y][position.x]
}
