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
import ru.hse.roguelike.state.InventoryState
import ru.hse.roguelike.ui.inventory.LanternaInventoryView
import ru.hse.roguelike.ui.window.LanternaGameWindow
import javax.swing.JFrame

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
            3,
            9,
            Position(0, 0),
            mutableListOf(
                ReusableItem("item1", "description1", ItemType.Body, 3),
                ReusableItem("item2", "description2", ItemType.Body, 4),
                ReusableItem("item3", "description3", ItemType.Weapon, 0),
                DisposableItem("item4", "description4") { health += 3 }
            )
        )
        val inventoryState = InventoryState(mapOf(), gameSound, inventoryView, hero)

        val gameInput = LanternaGameInput(terminal)
        val application = RoguelikeApplication(gameInput, inventoryState)
        application.run()
    }
}
