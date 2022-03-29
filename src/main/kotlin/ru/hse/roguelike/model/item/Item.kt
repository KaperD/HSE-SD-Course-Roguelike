package ru.hse.roguelike.model.item

import kotlinx.serialization.Serializable
import ru.hse.roguelike.model.creature.Hero
import kotlin.math.max

@Serializable
sealed class Item {
    abstract val id: String
    abstract val name: String
    abstract val description: String
    abstract val itemType: ItemType
    abstract val isUsed: Boolean
    abstract val healthChange: Int
    abstract val maximumHealthChange: Int

    open fun canApply(hero: Hero): Boolean = !isUsed

    open fun apply(hero: Hero) {
        hero.maximumHealth = max(1, hero.maximumHealth + maximumHealthChange)
        hero.health = max(1, hero.health + healthChange)
    }

    open fun cancel(hero: Hero) {
        hero.health = max(1, hero.health - healthChange)
        hero.maximumHealth = max(1, hero.maximumHealth - maximumHealthChange)
    }
}
