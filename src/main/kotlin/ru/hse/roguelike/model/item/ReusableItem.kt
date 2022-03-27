package ru.hse.roguelike.model.item

import ru.hse.roguelike.model.Hero
import kotlin.math.min

class ReusableItem(
    override val name: String,
    override val description: String,
    val itemType: ItemType,
    val bonusHealth: Int = 0
) : Item {
    private var _isUsed = false
    override val isUsed = _isUsed

    override fun canApply(hero: Hero): Boolean {
        return !isUsed && hero.items.filterIsInstance<ReusableItem>().none { it.isUsed && it.itemType == itemType }
    }

    override fun apply(hero: Hero) {
        _isUsed = true
        hero.maximumHealth += bonusHealth
    }

    override fun cancel(hero: Hero) {
        _isUsed = false
        hero.maximumHealth -= bonusHealth
        hero.health = min(hero.health, hero.maximumHealth)
    }
}
