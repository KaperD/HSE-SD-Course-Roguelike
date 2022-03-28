package ru.hse.roguelike.property

import ru.hse.roguelike.input.InputType
import ru.hse.roguelike.input.toInputType
import java.util.*

object StateProperties {
    val openMap: InputType
    val openMapFreeMode: InputType
    val openInventory: InputType
    val openHelp: InputType

    val inventoryItemUp: InputType
    val inventoryItemDown: InputType
    val inventoryItemAction: InputType

    val exitGame: InputType

    init {
        val properties: Properties = Properties().apply {
            load(InputType::class.java.getResourceAsStream("/state.properties"))
        }
        fun String.loadInputType() = properties.getProperty(this).toInputType()
        openMap = "open.map".loadInputType()
        openMapFreeMode = "open.map_free_mode".loadInputType()
        openInventory = "open.inventory".loadInputType()
        openHelp = "open.help".loadInputType()

        inventoryItemUp = "inventory.item.up".loadInputType()
        inventoryItemDown = "inventory.item.down".loadInputType()
        inventoryItemAction = "inventory.item.action".loadInputType()

        exitGame = "exit.game".loadInputType()
    }
}
