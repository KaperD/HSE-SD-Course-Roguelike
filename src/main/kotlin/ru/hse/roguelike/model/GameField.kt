package ru.hse.roguelike.model

/**
 * Прямоугольное игровое поле, состоящее из конечного числа клеток
 * @param field набор клеток игрового поля
 */
class GameField(
    private val field: List<List<Cell>>
) {
    /**
     * Размер поля по горизонтали
     */
    val width = field.firstOrNull()?.size ?: 0

    /**
     * Размер поля по вертикали
     */
    val height = field.size

    /**
     * Доступ к клетке
     * @param x координата по горизонтали
     * @param y координата по вертикали
     * @return  клетка с координатами x, y
     */
    fun get(x: Int, y: Int): Cell = field[y][x]

    /**
     * Доступ к клетке
     * @param position позиция клетке
     * @return         клетка с позицией position
     */
    fun get(position: Position): Cell = field[position.y][position.x]
}
