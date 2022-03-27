package ru.hse.roguelike.property

import ru.hse.roguelike.model.Cell
import ru.hse.roguelike.state.InputType
import ru.hse.roguelike.state.InputType.Companion.toInputType
import java.util.Properties

object StateProperties {
    val openMap: InputType
    val openMapFreeMode: InputType
    val openInventory: InputType
    val openHelp: InputType

    init {
        val properties = Properties().apply {
            load(Cell::class.java.getResourceAsStream("/application.properties"))
        }
        openMap = properties.getProperty("open.map").toInputType()
        openMapFreeMode = properties.getProperty("open.map_free_mode").toInputType()
        openInventory = properties.getProperty("open.inventory").toInputType()
        openHelp = properties.getProperty("open.help").toInputType()
    }
}
