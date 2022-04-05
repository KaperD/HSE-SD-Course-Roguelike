package ru.hse.roguelike.state

import ru.hse.roguelike.input.InputType
import ru.hse.roguelike.model.creature.Hero
import ru.hse.roguelike.property.StateProperties
import ru.hse.roguelike.sound.GameSound
import ru.hse.roguelike.ui.inventory.InventoryView
import kotlin.math.max
import kotlin.math.min

class InventoryState(
    private val hero: Hero,
    override val view: InventoryView,
    override val gameSound: GameSound,
    override val states: Map<InputType, State>
) : State() {
    private var chosenPosition = 0
    private val items = hero.items

    init {
        actionByInputType[StateProperties.inventoryItemUp] = {
            moveItemUp()
            this
        }
        actionByInputType[StateProperties.inventoryItemDown] = {
            moveItemDown()
            this
        }
        actionByInputType[StateProperties.inventoryItemAction] = {
            actionWithItem()
            this
        }
    }

    override fun activate() {
        chosenPosition = 0

        view.setItems(items, chosenPosition)
        view.setHeroStats(hero)

        view.show()
    }

    private fun moveItemUp() {
        if (chosenPosition  - 1 < 0) {
            gameSound.beep()
            return
        }
        chosenPosition -= 1
        view.setChosenItem(chosenPosition)
    }

    private fun moveItemDown() {
        if (chosenPosition + 1 > items.lastIndex) {
            gameSound.beep()
            return
        }
        chosenPosition += 1
        view.setChosenItem(chosenPosition)
    }

    private fun actionWithItem() {
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
    }
}
