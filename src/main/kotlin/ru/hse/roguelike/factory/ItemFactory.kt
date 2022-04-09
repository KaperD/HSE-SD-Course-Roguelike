package ru.hse.roguelike.factory

import ru.hse.roguelike.model.item.Item

/**
 * Фабрика, создающая предметы
 */
interface ItemFactory {
    /**
     * Получение предмета по id
     * @param itemId идентификатор предмета
     * @return предмет с идентификатором itemId
     */
    fun getById(itemId: String): Item

    /**
     * Получение случайного предмета
     * @return случайный предмет
     */
    fun getRandom(): Item
}
