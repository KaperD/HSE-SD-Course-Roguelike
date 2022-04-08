package ru.hse.roguelike.ui.inventory

import ru.hse.roguelike.model.creature.Hero
import ru.hse.roguelike.model.item.Item
import ru.hse.roguelike.ui.View

/**
 * Отображение инвентаря.
 * Показывает предметы, выбранный предмет, характеристики выбранного предмета и характеристики героя.
 */
interface InventoryView : View {
    /**
     * Установить предметы для отображения.
     * По умолчанию нет никаких предметов для отображения.
     * @param items          Предметы для отображения
     * @param chosenPosition Индекс выбранного элемента
     */
    fun setItems(items: List<Item>, chosenPosition: Int)

    /**
     * Установить в отображении характеристик героя
     * @param hero Герой, у которого смотреть характеристики
     */
    fun setHeroStats(hero: Hero)

    /**
     * Установить индекс выбранного элемента
     * @param position Индекс нового выбранного элемента
     */
    fun setChosenItem(position: Int)
}
