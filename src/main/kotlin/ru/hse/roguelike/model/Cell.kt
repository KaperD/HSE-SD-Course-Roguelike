package ru.hse.roguelike.model

import ru.hse.roguelike.model.creature.Creature
import ru.hse.roguelike.model.item.Item
import ru.hse.roguelike.property.StringProperties

data class Cell(
    val groundType: GroundType,
    val items: MutableList<Item>,
    var creature: Creature?
)

enum class GroundType(private val displayName: String) {
    Land(StringProperties.land),
    Water(StringProperties.water),
    Fire(StringProperties.fire),
    Stone(StringProperties.stone),
    LevelEnd(StringProperties.levelEnd);

    override fun toString(): String = displayName
}
