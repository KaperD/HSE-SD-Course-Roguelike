package ru.hse.roguelike.ui.map

import ru.hse.roguelike.model.Cell
import ru.hse.roguelike.model.creature.Hero
import ru.hse.roguelike.ui.View

/**
 * Отображение карты.
 * Показывает игровое поле и характеристики героя или клетки.
 */
interface MapView : View {
    /**
     * Установить клетку в конкретной позиции.
     * @param x    Позиция в отображении по горизонтали
     * @param y    Позиция в отображении по вертикали
     * @param cell Клетка для установки
     */
    fun set(x: Int, y: Int, cell: Cell)

    /**
     * Установить клетку в конкретной позиции с подсветкой.
     * @param x    Позиция в отображении по горизонтали
     * @param y    Позиция в отображении по вертикали
     * @param cell Клетка для установки
     */
    fun setHighlighted(x: Int, y: Int, cell: Cell)

    /**
     * Установить в отображении характеристики героя.
     * @param hero Герой, у которого брать характеристики
     */
    fun setHeroStats(hero: Hero)

    /**
     * Установить в отображении информацию о клетке
     * @param cell Клетка, из которой брать информацию
     */
    fun setCellInfo(cell: Cell)
}
