package ru.hse.roguelike.property

import ru.hse.roguelike.state.InputType
import ru.hse.roguelike.state.toInputType
import java.util.*


class StateProperties(propertiesFileName: String = "/application.properties") {
    val openMap: InputType
    val openMapFreeMode: InputType
    val openInventory: InputType
    val openHelp: InputType

    val inventoryItemUp: InputType
    val inventoryItemDown: InputType
    val inventoryItemAction: InputType

    init {
        val properties: Properties = Properties().apply {
            load(StateProperties::class.java.getResourceAsStream(propertiesFileName))
        }
        openMap = properties.getProperty("open.map").toInputType()
        openMapFreeMode = properties.getProperty("open.map_free_mode").toInputType()
        openInventory = properties.getProperty("open.inventory").toInputType()
        openHelp = properties.getProperty("open.help").toInputType()

        inventoryItemUp = properties.getProperty("inventory.item.up").toInputType()
        inventoryItemDown = properties.getProperty("inventory.item.down").toInputType()
        inventoryItemAction = properties.getProperty("inventory.item.action").toInputType()
    }
}
