package ru.hse.roguelike

import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame
import ru.hse.roguelike.input.LanternaGameInput
import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.Hero
import ru.hse.roguelike.model.item.DisposableItem
import ru.hse.roguelike.model.item.ItemType
import ru.hse.roguelike.model.item.ReusableItem
import ru.hse.roguelike.property.SizePropertiesImpl
import ru.hse.roguelike.property.StateProperties.openHelp
import ru.hse.roguelike.property.StateProperties.openInventory
import ru.hse.roguelike.state.HelpState
import ru.hse.roguelike.state.InventoryState
import ru.hse.roguelike.ui.help.LanternaHelpView
import ru.hse.roguelike.ui.inventory.LanternaInventoryView
import ru.hse.roguelike.ui.window.LanternaGameWindow
import javax.swing.JFrame

const val initHealth = 100
const val initX = 0
const val initY = 0
const val bonus1 = 3
const val bonus2 = 4
const val bonus3 = 0
const val bonus4 = 3

fun main() {
    val sizeProperties = SizePropertiesImpl()
    val mapWidth = sizeProperties.mapWidth
    val mapHeight = sizeProperties.mapHeight
    val imageWidth = sizeProperties.imageWidth
    val imageHeight = sizeProperties.imageHeight
    val factory = DefaultTerminalFactory()
    factory.createTerminal().use { terminal ->
        if (terminal is SwingTerminalFrame) {
            terminal.extendedState = JFrame.MAXIMIZED_BOTH
        }
        terminal.enterPrivateMode()
        val window = LanternaGameWindow(terminal, imageWidth, imageHeight)

        val gameSound = GameSound(terminal)
        val inventoryView = LanternaInventoryView(window)
        val hero = Hero(
            initHealth,
            initHealth,
            Position(initX, initY),
            mutableListOf(
                ReusableItem("item1", "description1", ItemType.Body, bonus1),
                ReusableItem("item2", "description2", ItemType.Body, bonus2),
                ReusableItem("item3", "description3", ItemType.Weapon, bonus3),
                DisposableItem("item4", "description4") { health += bonus4 }
            )
        )
        val inventoryState = InventoryState(gameSound, inventoryView, hero)
        val helpState = HelpState(gameSound, LanternaHelpView(window))

        val states = mapOf(
            openHelp to helpState,
            openInventory to inventoryState
        )

        inventoryState.states = states
        helpState.states = states

        val gameInput = LanternaGameInput(terminal)
        val application = RoguelikeApplication(gameInput, inventoryState)
        application.run()
    }
}
