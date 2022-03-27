package ru.hse.roguelike.state

import ru.hse.roguelike.GameSound
import ru.hse.roguelike.model.GameModel
import ru.hse.roguelike.property.StateProperties.Companion.inventoryItemAction
import ru.hse.roguelike.property.StateProperties.Companion.inventoryItemDown
import ru.hse.roguelike.property.StateProperties.Companion.inventoryItemUp
import ru.hse.roguelike.property.StateProperties.Companion.openMap
import ru.hse.roguelike.ui.inventory.InventoryView
import kotlin.math.max
import kotlin.math.min

class InventoryState(
    private val states: Map<InputType, State>,
    private val gameSound: GameSound,
    private val inventoryView: InventoryView,
    gameModel: GameModel
) : State {
    private var chosenPosition = 0
    private val items = gameModel.hero.items
    private val hero = gameModel.hero

    override fun handleInput(type: InputType): State {
        return when (type) {
            openMap -> states[openMap]!!
            inventoryItemUp -> {
                chosenPosition = max(0, chosenPosition - 1)
                inventoryView.setChosenItem(chosenPosition)

                this
            }
            inventoryItemDown -> {
                chosenPosition = min(items.lastIndex, chosenPosition + 1)
                inventoryView.setChosenItem(chosenPosition)

                this
            }
            inventoryItemAction -> {
                val item = items[chosenPosition]
                if (item.isUsed) {
                    item.cancel(hero)
                } else if (item.canApply(hero)) {
                    item.apply(hero)

                    chosenPosition = min(items.lastIndex, chosenPosition)
                }

                inventoryView.setItems(items, chosenPosition)
                inventoryView.setHeroStats(hero)

                this
            }
            else -> {
                gameSound.beep()
                this
            }
        }.also {
            inventoryView.show()
        }
    }

    override fun activate() {
        chosenPosition = 0

        inventoryView.setItems(items, chosenPosition)
        inventoryView.setHeroStats(hero)

        inventoryView.show()
    }
}
