package ru.hse.roguelike.model.item

import ru.hse.roguelike.model.Hero

class DisposableItem(
    override val name: String,
    override val description: String,
    private val action: Hero.() -> Unit
) : Item {
    private var _isUsed = false
    override val isUsed = _isUsed

    override fun canApply(hero: Hero): Boolean {
        require(!isUsed)
        return true
    }

    override fun apply(hero: Hero) {
        hero.action()
        _isUsed = true
    }

    override fun cancel(hero: Hero) {
        throw IllegalStateException("Can't cancel disposable item")
    }
}
