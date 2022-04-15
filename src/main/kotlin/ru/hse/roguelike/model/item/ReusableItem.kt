package ru.hse.roguelike.model.item

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.hse.roguelike.model.creature.Hero

/**
 * Многоразовый предмет.
 * Предмет можно применить с последующей отменой неограниченное количество раз.
 */
@Serializable
@SerialName("Reusable")
data class ReusableItem(
    override val id: String,
    override val name: String,
    override val description: String,
    override val itemType: ItemType,
    override val healthChange: Int = 0,
    override val maximumHealthChange: Int = 0,
    override val attackDamageChange: Int = 0
) : Item() {
    override var isUsed = false

    init {
        require(itemType != ItemType.Disposable) { "Reusable item can't have disposable type" }
    }

    override fun canApply(hero: Hero): Boolean {
        return !isUsed && hero.items.none { it.isUsed && it.itemType == itemType }
    }

    override fun apply(hero: Hero) {
        super.apply(hero)
        isUsed = true
    }

    override fun cancel(hero: Hero) {
        super.cancel(hero)
        isUsed = false
    }

    override fun clone(): Item = this.copy()
}
