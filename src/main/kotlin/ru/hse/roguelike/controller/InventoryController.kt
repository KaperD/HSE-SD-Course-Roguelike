package ru.hse.roguelike.controller

import ru.hse.roguelike.model.creature.Hero
import ru.hse.roguelike.sound.GameSound
import ru.hse.roguelike.ui.inventory.InventoryView
import kotlin.math.min

/**
 * Содержит логику просмотра предметов в инвентаре, использование и отмену использования предметов
 */
class InventoryController(
    private val hero: Hero,
    private val view: InventoryView,
    private val gameSound: GameSound,
) {
    private var chosenPosition = 0
    private val items = hero.items

    /**
     * Перерисовать всё представление и сделать выбранным первый предмет (если он есть)
     */
    fun showInventory() {
        chosenPosition = 0

        view.setItems(items, chosenPosition)
        view.setHeroStats(hero)

        view.show()
    }

    fun moveItemUp() {
        if (chosenPosition - 1 < 0) {
            gameSound.beep()
            return
        }
        chosenPosition -= 1
        view.setChosenItem(chosenPosition)
        view.show()
    }

    fun moveItemDown() {
        if (chosenPosition + 1 > items.lastIndex) {
            gameSound.beep()
            return
        }
        chosenPosition += 1
        view.setChosenItem(chosenPosition)
        view.show()
    }

    fun actionWithItem() {
        if (items.isEmpty()) {
            gameSound.beep()
            return
        }
        val item = items[chosenPosition]
        if (item.isUsed) {
            item.cancel(hero)
        } else if (item.canApply(hero)) {
            item.apply(hero)

            chosenPosition = min(items.lastIndex, chosenPosition)
        } else {
            gameSound.beep()
            return
        }

        view.setItems(items, chosenPosition)
        view.setHeroStats(hero)
        view.show()
    }
}
