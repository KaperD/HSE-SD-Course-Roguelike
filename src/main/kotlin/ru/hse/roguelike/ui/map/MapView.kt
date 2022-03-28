package ru.hse.roguelike.ui.map

import ru.hse.roguelike.model.Cell
import ru.hse.roguelike.model.creature.Hero

interface MapView {
    fun set(x: Int, y: Int, cell: Cell)
    fun setHighlighted(x: Int, y: Int, cell: Cell)
    fun setHeroStats(hero: Hero)
    fun setCellInfo(cell: Cell)
    fun show()
}
