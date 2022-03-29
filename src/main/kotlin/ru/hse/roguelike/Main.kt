package ru.hse.roguelike

import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import ru.hse.roguelike.factory.GameFieldFactoryImpl
import ru.hse.roguelike.factory.ItemFactoryImpl
import ru.hse.roguelike.input.InputType
import ru.hse.roguelike.input.LanternaGameInput
import ru.hse.roguelike.model.Cell
import ru.hse.roguelike.model.GameModel
import ru.hse.roguelike.model.creature.Hero
import ru.hse.roguelike.model.item.Item
import ru.hse.roguelike.property.GamePropertiesImpl
import ru.hse.roguelike.property.StateProperties.openHelp
import ru.hse.roguelike.property.StateProperties.openInventory
import ru.hse.roguelike.property.StateProperties.openMap
import ru.hse.roguelike.property.StateProperties.openMapFreeMode
import ru.hse.roguelike.sound.LanternaGameSound
import ru.hse.roguelike.state.*
import ru.hse.roguelike.ui.help.LanternaMessageView
import ru.hse.roguelike.ui.inventory.LanternaInventoryView
import ru.hse.roguelike.ui.map.LanternaMapView
import ru.hse.roguelike.ui.window.LanternaGameWindow
import javax.swing.JFrame

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

        val itemFactory = ItemFactoryImpl()
        val gameFieldFactory = GameFieldFactoryImpl(mapWidth, mapHeight, itemFactory)

        val (field, heroPosition) = gameFieldFactory.getByLevel(1)
        val hero = Hero(
            gameProperties.initialHeroHealth,
            gameProperties.initialHeroHealth,
            heroPosition,
            items
        )
        field.get(heroPosition).creature = hero

        val gameModel = GameModel(field, hero)

        val states = mutableMapOf<InputType, State>()

        val inventoryState = InventoryState(hero, inventoryView, gameSound, states)
        val helpState = HelpState(LanternaMessageView(window), gameSound, states)
        val gameOverState = GameOverState(LanternaMessageView(window), gameSound)
        val mapFreeModeState =
            MapFreeModeState(gameModel, LanternaMapView(window, mapWidth, mapHeight), gameSound, states)

        states.putAll(
            mapOf(
                openHelp to helpState,
                openInventory to inventoryState,
                openMap to gameOverState,
                openMapFreeMode to mapFreeModeState
            )
        )

        val gameInput = LanternaGameInput(terminal)
        val application = RoguelikeApplication(gameInput, inventoryState)
        application.run()
    }
}
