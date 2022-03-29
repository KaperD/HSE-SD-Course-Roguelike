package ru.hse.roguelike.ui.inventory

import ru.hse.roguelike.model.creature.Hero
import ru.hse.roguelike.model.item.Item
import ru.hse.roguelike.ui.View

interface InventoryView : View {
    fun setItems(items: List<Item>, chosenPosition: Int)
    fun setHeroStats(hero: Hero)
    fun setChosenItem(position: Int)
}
