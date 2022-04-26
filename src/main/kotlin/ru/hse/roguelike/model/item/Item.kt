package ru.hse.roguelike.model.item

import kotlinx.serialization.Serializable
import ru.hse.roguelike.model.creature.Hero
import kotlin.math.max

/**
 * Предмет
 * @property id уникальное значение, по которому можно идентифицировать предмет
 */
@Serializable
sealed class Item {
    abstract val id: String
    abstract val name: String
    abstract val description: String
    abstract val itemType: ItemType
    abstract val isUsed: Boolean
    abstract val healthChange: Int
    abstract val maximumHealthChange: Int
    abstract val attackDamageChange: Int

    /**
     * Проверяет возможность применить данный предмет к герою
     * @param hero герой, к которому хотим применить предмет
     * @return результат проверки
     */
    open fun canApply(hero: Hero): Boolean = !isUsed

    /**
     * Применение предмета к герою
     * @param hero герой, к которому нужно применить предмет
     */
    open fun apply(hero: Hero) {
        require(canApply(hero)) { "Can't apply item" }
        hero.maximumHealth = max(1, hero.maximumHealth + maximumHealthChange)
        hero.health = max(1, hero.health + healthChange)
        hero.attackDamage += attackDamageChange
    }

    /**
     * Отмена эффектов предмета у героя, к которому данный предмет был ранее применён.
     * Например, происходит при снятии предмета с героя.
     * @param hero герой, с которого нужно снять эффекты данного предмета
     */
    open fun cancel(hero: Hero) {
        hero.health = max(1, hero.health - healthChange)
        hero.maximumHealth = max(1, hero.maximumHealth - maximumHealthChange)
        hero.attackDamage -= attackDamageChange
    }

    abstract fun clone(): Item
}
