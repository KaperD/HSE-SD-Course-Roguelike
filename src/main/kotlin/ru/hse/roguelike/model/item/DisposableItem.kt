package ru.hse.roguelike.model.item

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.hse.roguelike.model.creature.Hero

@Serializable
@SerialName("Disposable")
data class DisposableItem(
    override val id: String,
    override val name: String,
    override val description: String,
    override val healthChange: Int = 0,
    override val maximumHealthChange: Int = 0
) : Item() {
    override val itemType: ItemType = ItemType.Disposable
    override var isUsed = false

    override fun canApply(hero: Hero): Boolean {
        require(!isUsed) { "Should not check disposed item" }
        return true
    }

    override fun apply(hero: Hero) {
        super.apply(hero)
        hero.items.remove(this)
        isUsed = true
    }

    override fun cancel(hero: Hero) {
        throw IllegalStateException("Can't cancel disposable item")
    }
}
