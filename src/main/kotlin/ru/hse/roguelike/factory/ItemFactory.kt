package ru.hse.roguelike.factory

import ru.hse.roguelike.model.item.Item

interface ItemFactory {
    fun getById(itemId: String): Item
    fun getRandom(): Item
}
