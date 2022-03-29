package ru.hse.roguelike

import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import ru.hse.roguelike.input.LanternaGameInput
import ru.hse.roguelike.model.Cell
import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.Hero
import ru.hse.roguelike.model.item.Item
import ru.hse.roguelike.property.GamePropertiesImpl
import ru.hse.roguelike.property.StateProperties.openHelp
import ru.hse.roguelike.property.StateProperties.openInventory
import ru.hse.roguelike.property.StateProperties.openMap
import ru.hse.roguelike.sound.LanternaGameSound
import ru.hse.roguelike.state.GameOverState
import ru.hse.roguelike.state.HelpState
import ru.hse.roguelike.state.InventoryState
import ru.hse.roguelike.ui.help.LanternaMessageView
import ru.hse.roguelike.ui.inventory.LanternaInventoryView
import ru.hse.roguelike.ui.window.LanternaGameWindow
import javax.swing.JFrame

const val initX = 0
const val initY = 0

fun main() {
    val items: MutableList<Item> = Json.decodeFromString(
        Cell::class.java.getResourceAsStream("/items.json").reader().readText()
    )

    val gameProperties = GamePropertiesImpl()
    val mapWidth = gameProperties.mapWidth
    val mapHeight = gameProperties.mapHeight
    val imageWidth = gameProperties.imageWidth
    val imageHeight = gameProperties.imageHeight
    val factory = DefaultTerminalFactory()
    factory.createTerminal().use { terminal ->
        if (terminal is SwingTerminalFrame) {
            terminal.extendedState = JFrame.MAXIMIZED_BOTH
        }
        terminal.enterPrivateMode()
        val window = LanternaGameWindow(terminal, imageWidth, imageHeight)

        val gameSound = LanternaGameSound(terminal)
        val inventoryView = LanternaInventoryView(window)
        val hero = Hero(
            gameProperties.initialHeroHealth,
            gameProperties.initialHeroHealth,
            Position(initX, initY),
            items
        )
        val inventoryState = InventoryState(gameSound, inventoryView, hero)
        val helpState = HelpState(gameSound, LanternaMessageView(window))
        val gameOverState = GameOverState(LanternaMessageView(window))

        val states = mapOf(
            openHelp to helpState,
            openInventory to inventoryState,
            openMap to gameOverState
        )

        inventoryState.states = states
        helpState.states = states

        val gameInput = LanternaGameInput(terminal)
        val application = RoguelikeApplication(gameInput, inventoryState)
        application.run()
    }
}
