package ru.hse.roguelike.property

import ru.hse.roguelike.input.InputType
import ru.hse.roguelike.input.toInputType
import java.util.*

/**
 * Определение состояний игры.
 * Данные загружаются из файла state.properties
 */
object StateProperties {
    val openMap: InputType
    val openMapFreeMode: InputType
    val openInventory: InputType
    val openHelp: InputType

    val inventoryItemUp: InputType
    val inventoryItemDown: InputType
    val inventoryItemAction: InputType

    val moveUp: InputType
    val moveDown: InputType
    val moveLeft: InputType
    val moveRight: InputType

    val freeModeCursorUp: InputType
    val freeModeCursorDown: InputType
    val freeModeCursorLeft: InputType
    val freeModeCursorRight: InputType

    val exitGame: InputType

    init {
        val properties: Properties = Properties().apply {
            load(InputType::class.java.getResourceAsStream("/state.properties")!!.reader())
        }
        fun String.loadInputType() = properties.getProperty(this).toInputType()
        openMap = "open.map".loadInputType()
        openMapFreeMode = "open.map_free_mode".loadInputType()
        openInventory = "open.inventory".loadInputType()
        openHelp = "open.help".loadInputType()

        inventoryItemUp = "inventory.item.up".loadInputType()
        inventoryItemDown = "inventory.item.down".loadInputType()
        inventoryItemAction = "inventory.item.action".loadInputType()

        moveUp = "move.up".loadInputType()
        moveDown = "move.down".loadInputType()
        moveLeft = "move.left".loadInputType()
        moveRight = "move.right".loadInputType()

        freeModeCursorUp = moveUp
        freeModeCursorDown = moveDown
        freeModeCursorLeft = moveLeft
        freeModeCursorRight = moveRight

        exitGame = "exit.game".loadInputType()
    }
}
