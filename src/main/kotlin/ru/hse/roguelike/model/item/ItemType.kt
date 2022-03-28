package ru.hse.roguelike.model.item

import ru.hse.roguelike.property.StringProperties

enum class ItemType(private val displayName: String) {
    Head(StringProperties.head),
    Body(StringProperties.body),
    Legs(StringProperties.legs),
    Hands(StringProperties.hands),
    Weapon(StringProperties.weapon),
    Disposable(StringProperties.disposable);

    override fun toString(): String = displayName
}
