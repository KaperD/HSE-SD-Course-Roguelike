package ru.hse.roguelike

import com.googlecode.lanterna.TerminalSize
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import ru.hse.roguelike.factory.item.ItemFactoryImpl
import ru.hse.roguelike.input.InputType
import ru.hse.roguelike.input.LanternaGameInput
import ru.hse.roguelike.model.GameField
import ru.hse.roguelike.model.GameModel
import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.Hero
import ru.hse.roguelike.property.GamePropertiesImpl
import ru.hse.roguelike.property.StateProperties.openHelp
import ru.hse.roguelike.property.StateProperties.openInventory
import ru.hse.roguelike.property.StateProperties.openMap
import ru.hse.roguelike.property.StateProperties.openMapFreeMode
import ru.hse.roguelike.sound.LanternaGameSound
import ru.hse.roguelike.state.*
import ru.hse.roguelike.ui.inventory.LanternaInventoryView
import ru.hse.roguelike.ui.map.LanternaMapView
import ru.hse.roguelike.ui.message.LanternaMessageView
import ru.hse.roguelike.ui.window.LanternaGameWindow

fun main(args: Array<String>) {
    val gameProperties = GamePropertiesImpl()
    val mapWidth = gameProperties.mapWidth
    val mapHeight = gameProperties.mapHeight
    val imageWidth = gameProperties.imageWidth
    val imageHeight = gameProperties.imageHeight
    val factory = DefaultTerminalFactory()
    factory.setInitialTerminalSize(TerminalSize(imageWidth + 2, imageHeight + 2))
    val gameTerminal = if (args.firstOrNull() == "--window") {
        factory.createTerminalEmulator()
    } else {
        factory.createTerminal()
    }
    gameTerminal.use { terminal ->
        terminal.enterPrivateMode()
        val window = LanternaGameWindow(terminal, imageWidth, imageHeight)

        val gameSound = LanternaGameSound(terminal)
        val inventoryView = LanternaInventoryView(window)

        val itemFactory = ItemFactoryImpl()

        val hero = Hero(
            gameProperties.initialHeroHealth,
            gameProperties.initialHeroHealth,
            gameProperties.initialHeroAttackDamage,
            Position(0, 0),
            mutableListOf()
        )

        val gameModel = GameModel(GameField(emptyList()), mutableListOf(), hero)

        val states = mutableMapOf<InputType, State>()

        val inventoryState = InventoryState(hero, inventoryView, gameSound, states)
        val helpState = HelpState(LanternaMessageView(window), gameSound, states)
        val mapFreeModeState =
            MapFreeModeState(gameModel, LanternaMapView(window, mapWidth, mapHeight), gameSound, states)
        val gameOverState = GameOverState(LanternaMessageView(window), gameSound)
        val victoryState = VictoryState(LanternaMessageView(window), gameSound)
        val mapState = MapState(
            gameModel,
            LanternaMapView(window, mapWidth, mapHeight),
            gameSound,
            states,
            itemFactory,
            gameProperties,
            gameOverState,
            victoryState
        )

        states.putAll(
            mapOf(
                openHelp to helpState,
                openInventory to inventoryState,
                openMap to mapState,
                openMapFreeMode to mapFreeModeState
            )
        )

        val gameInput = LanternaGameInput(terminal)
        val application = RoguelikeApplication(gameInput, mapState)
        application.run()
    }
}
