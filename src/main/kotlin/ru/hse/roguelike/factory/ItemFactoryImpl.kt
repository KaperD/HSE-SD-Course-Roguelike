package ru.hse.roguelike.factory

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import ru.hse.roguelike.model.item.Item

/**
 * Фабрика, создающая предмет, описанные в соответствующем файле.
 * @param itemsFileName название файла с описанием предметов
 */
class ItemFactoryImpl(itemsFileName: String = "/items.json") : ItemFactory {
    private val items: Map<String, Item>

    init {
        val itemsList: List<Item> = Json.decodeFromString(
            ItemFactoryImpl::class.java.getResourceAsStream(itemsFileName)!!.reader().readText()
        )
        items = itemsList.associateBy { it.id }
    }

    /**
     * Получение предмета, описанного в файле, по id
     * @param itemId идентификатор предмета
     * @return предмет с идентификатором itemId
     */
    override fun getById(itemId: String): Item {
        return items.getOrElse(itemId) { throw IllegalStateException("Unknown item id") }.clone()
    }

    /**
     * Получение случайного предмета среди предметов описанных в файле
     * @return случайный предмет
     */
    override fun getRandom(): Item {
        return items.values.random().clone()
    }
}
