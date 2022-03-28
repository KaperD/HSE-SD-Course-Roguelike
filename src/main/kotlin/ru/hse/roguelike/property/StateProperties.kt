package ru.hse.roguelike.property

import ru.hse.roguelike.state.InputType
import ru.hse.roguelike.state.toInputType
import java.util.*


interface StateProperties {
    val openMap: InputType
    val openMapFreeMode: InputType
    val openInventory: InputType
    val openHelp: InputType

    val inventoryItemUp: InputType
    val inventoryItemDown: InputType
    val inventoryItemAction: InputType
}

class StatePropertiesImpl(propertiesFileName: String = "/state.properties") : StateProperties {
    override val openMap: InputType
    override val openMapFreeMode: InputType
    override val openInventory: InputType
    override val openHelp: InputType

    override val inventoryItemUp: InputType
    override val inventoryItemDown: InputType
    override val inventoryItemAction: InputType

    init {
        val properties: Properties = Properties().apply {
            load(StateProperties::class.java.getResourceAsStream(propertiesFileName))
        }
        fun String.loadInputType() = properties.getProperty(this).toInputType()
        openMap = "open.map".loadInputType()
        openMapFreeMode = "open.map_free_mode".loadInputType()
        openInventory = "open.inventory".loadInputType()
        openHelp = "open.help".loadInputType()

        inventoryItemUp = "inventory.item.up".loadInputType()
        inventoryItemDown = "inventory.item.down".loadInputType()
        inventoryItemAction = "inventory.item.action".loadInputType()
    }
}
