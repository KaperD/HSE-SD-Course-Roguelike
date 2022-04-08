package ru.hse.roguelike.model

import ru.hse.roguelike.model.creature.Creature
import ru.hse.roguelike.model.item.Item
import ru.hse.roguelike.property.StringProperties

/**
 * Клетка игрового поля. Может содержать не более одного существа.
 * @param groundType тип клетки
 * @param items      предметы, находящиеся на данной клетке
 * @param creature   существо стоящее на клетке
 */
data class Cell(
    val groundType: GroundType,
    val items: MutableList<Item>,
    var creature: Creature?
)

/**
 * Тип клетки
 */
enum class GroundType(private val displayName: String) {
    Land(StringProperties.land),
    Water(StringProperties.water),
    Fire(StringProperties.fire),
    Stone(StringProperties.stone),
    LevelEnd(StringProperties.levelEnd);

    override fun toString(): String = displayName
}
