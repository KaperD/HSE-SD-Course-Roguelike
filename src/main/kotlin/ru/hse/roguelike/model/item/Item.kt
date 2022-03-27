package ru.hse.roguelike.model.item

import ru.hse.roguelike.model.Hero

interface Item {
    val isUsed: Boolean
    val name: String
    val description: String

    fun canApply(hero: Hero): Boolean
    fun apply(hero: Hero)
    fun cancel(hero: Hero)
}
