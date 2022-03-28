package ru.hse.roguelike.state

import ru.hse.roguelike.input.InputType
import ru.hse.roguelike.model.creature.Hero
import ru.hse.roguelike.property.StateProperties
import ru.hse.roguelike.sound.GameSound
import ru.hse.roguelike.ui.inventory.InventoryView
import kotlin.math.max
import kotlin.math.min

class InventoryState(
    private val gameSound: GameSound,
    private val inventoryView: InventoryView,
    private val hero: Hero,
) : State {
    private var chosenPosition = 0
    private val items = hero.items
    private val actionByInputType = mutableMapOf<InputType, () -> State>()

    var states: Map<InputType, State> = mapOf()

    init {
        actionByInputType[StateProperties.inventoryItemUp] = this::moveItemUp
        actionByInputType[StateProperties.inventoryItemDown] = this::moveItemDown
        actionByInputType[StateProperties.inventoryItemAction] = this::actionWithItem
    }

    override fun handleInput(type: InputType): State {
        val action = actionByInputType[type]
        val newState = states[type]
        return if (action != null) {
            action().also {
                inventoryView.show()
            }
        } else if (newState != null) {
            newState
        } else {
            gameSound.beep()
            this
        }
    }

    private fun moveItemUp(): State {
        chosenPosition = max(0, chosenPosition - 1)
        inventoryView.setChosenItem(chosenPosition)

        return this
    }

    private fun moveItemDown(): State {
        chosenPosition = min(items.lastIndex, chosenPosition + 1)
        inventoryView.setChosenItem(chosenPosition)

        return this
    }

    private fun actionWithItem(): State {
        val item = items[chosenPosition]
        if (item.isUsed) {
            item.cancel(hero)
        } else if (item.canApply(hero)) {
            item.apply(hero)

            chosenPosition = min(items.lastIndex, chosenPosition)
        } else {
            gameSound.beep()
        }

        inventoryView.setItems(items, chosenPosition)
        inventoryView.setHeroStats(hero)

        return this
    }

    override fun activate() {
        chosenPosition = 0

        inventoryView.setItems(items, chosenPosition)
        inventoryView.setHeroStats(hero)

        inventoryView.show()
    }
}
