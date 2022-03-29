package ru.hse.roguelike.factory

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import ru.hse.roguelike.model.item.Item

class ItemFactoryImpl(itemsFileName: String = "/items.json") : ItemFactory {
    private val items: Map<String, Item>

    init {
        val itemsList: List<Item> = Json.decodeFromString(
            ItemFactoryImpl::class.java.getResourceAsStream(itemsFileName)!!.reader().readText()
        )
        items = itemsList.associateBy { it.id }
    }

    override fun getById(itemId: String): Item {
        return items.getOrElse(itemId) { throw IllegalStateException("Unknown item id") }
    }

    override fun getRandom(): Item {
        return items.values.random()
    }
}
