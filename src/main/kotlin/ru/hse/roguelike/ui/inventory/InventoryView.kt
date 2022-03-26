package ru.hse.roguelike.ui.inventory

import ru.hse.roguelike.model.Hero
import ru.hse.roguelike.model.Item

interface InventoryView {
    fun setItems(items: List<Item>)
    fun setHeroStats(hero: Hero)
    fun setChosenItem(position: Int)
    fun show()
}
