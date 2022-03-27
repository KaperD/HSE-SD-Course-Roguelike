package ru.hse.roguelike.property

import ru.hse.roguelike.state.InputType
import ru.hse.roguelike.state.InputType.Companion.toInputType
import java.util.Properties

class StateProperties {
    companion object {
        private val properties: Properties = Properties().apply {
            load(StateProperties::class.java.getResourceAsStream("/application.properties"))
        }

        val openMap: InputType = properties.getProperty("open.map").toInputType()
        val openMapFreeMode: InputType = properties.getProperty("open.map_free_mode").toInputType()
        val openInventory: InputType = properties.getProperty("open.inventory").toInputType()
        val openHelp: InputType = properties.getProperty("open.help").toInputType()

        val inventoryItemUp: InputType = properties.getProperty("inventory.item.up").toInputType()
        val inventoryItemDown: InputType = properties.getProperty("inventory.item.down").toInputType()
        val inventoryItemAction: InputType = properties.getProperty("inventory.item.action").toInputType()
    }
}
