package ru.hse.roguelike.state

import ru.hse.roguelike.controller.InventoryController
import ru.hse.roguelike.input.InputType
import ru.hse.roguelike.property.StateProperties
import ru.hse.roguelike.sound.GameSound

/**
 * Состояние работы с инвентарем
 */
class InventoryState(
    private val inventoryController: InventoryController,
    override val gameSound: GameSound,
    override val states: Map<InputType, State>
) : State() {

    init {
        actionByInputType[StateProperties.inventoryItemUp] = {
            inventoryController.moveItemUp()
            this
        }
        actionByInputType[StateProperties.inventoryItemDown] = {
            inventoryController.moveItemDown()
            this
        }
        actionByInputType[StateProperties.inventoryItemAction] = {
            inventoryController.actionWithItem()
            this
        }
    }

    override fun activate() {
        inventoryController.showInventory()
    }
}
