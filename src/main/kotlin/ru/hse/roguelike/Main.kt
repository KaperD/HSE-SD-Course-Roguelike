package ru.hse.roguelike

import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame
import ru.hse.roguelike.input.LanternaGameInput
import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.Hero
import ru.hse.roguelike.model.item.DisposableItem
import ru.hse.roguelike.model.item.ItemType
import ru.hse.roguelike.model.item.ReusableItem
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
const val bonus1 = 3
const val bonus2 = 4
const val bonus3 = 2

fun main() {
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
            mutableListOf(
                ReusableItem("item1", "description1", ItemType.Body, maximumHealthChange = bonus1),
                ReusableItem("item2", "description2", ItemType.Body, bonus2),
                ReusableItem("item3", "description3", ItemType.Weapon, bonus3),
                DisposableItem("item4", "description4", 2)
            )
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
