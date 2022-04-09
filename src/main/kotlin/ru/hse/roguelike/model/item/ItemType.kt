package ru.hse.roguelike.model.item

import ru.hse.roguelike.property.StringProperties

/**
 * Тип предмета.
 */
enum class ItemType(private val displayName: String) {
    Head(StringProperties.head),
    Body(StringProperties.body),
    Boots(StringProperties.boots),
    Hands(StringProperties.hands),
    Weapon(StringProperties.weapon),
    Disposable(StringProperties.disposable);

    override fun toString(): String = displayName
}
