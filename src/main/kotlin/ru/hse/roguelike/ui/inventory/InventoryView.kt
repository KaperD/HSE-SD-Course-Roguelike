package ru.hse.roguelike.ui.inventory

import ru.hse.roguelike.model.creature.Hero
import ru.hse.roguelike.model.item.Item

interface InventoryView {
    fun setItems(items: List<Item>, chosenPosition: Int)
    fun setHeroStats(hero: Hero)
    fun setChosenItem(position: Int)
    fun show()
}
