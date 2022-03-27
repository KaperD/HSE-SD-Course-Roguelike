package ru.hse.roguelike

import com.googlecode.lanterna.input.KeyType
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame
import ru.hse.roguelike.model.Cell
import ru.hse.roguelike.model.GameField
import ru.hse.roguelike.model.GameModel
import ru.hse.roguelike.model.GroundType
import ru.hse.roguelike.model.Hero
import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.item.DisposableItem
import ru.hse.roguelike.model.item.ItemType
import ru.hse.roguelike.model.item.ReusableItem
import ru.hse.roguelike.property.Properties
import ru.hse.roguelike.property.StateProperties
import ru.hse.roguelike.state.InventoryState
import ru.hse.roguelike.ui.inventory.LanternaInventoryView
import ru.hse.roguelike.ui.map.LanternaMapView
import ru.hse.roguelike.ui.map.MapView
import ru.hse.roguelike.ui.window.LanternaGameWindow
import javax.swing.JFrame

fun main() {
    val mapWidth = Properties.mapWidth
    val mapHeight = Properties.mapHeight
    val imageWidth = Properties.imageWidth
    val imageHeight = Properties.imageHeight
    val factory = DefaultTerminalFactory()
    factory.createTerminal().use { terminal ->
        if (terminal is SwingTerminalFrame) {
            terminal.extendedState = JFrame.MAXIMIZED_BOTH
        }
        terminal.enterPrivateMode()
        val window = LanternaGameWindow(terminal, imageWidth, imageHeight)
        val mapView = LanternaMapView(window, mapWidth, mapHeight)
        setup(mapView)
        var x = 0
        var y = 0

        val gameSound = GameSound(terminal)
        val inventoryView = LanternaInventoryView(window)
        val gameModel = GameModel(
            GameField(map),
            Hero(
                3,
                9,
                Position(x, y),
                mutableListOf(
                    ReusableItem("item1", "description1", ItemType.Body, 3),
                    DisposableItem("item2", "description2") { health += 3 }
                )
            )
        )
        val inventoryState = InventoryState(mapOf(), gameSound, inventoryView, gameModel)

        inventoryState.activate()

        while (true) {
//            mapView.setHighlighted(x, y, map[y][x])
//            mapView.setCellInfo(map[y][x])
//            mapView.show()

            when (terminal.readInput().keyType) {
                KeyType.Escape, KeyType.EOF -> break
                KeyType.ArrowUp -> {
//                    mapView.set(x, y, map[y][x])
//                    y = max(0, y - 1)
                    inventoryState.handleInput(StateProperties.inventoryItemUp)
                }
                KeyType.ArrowDown -> {
//                    mapView.set(x, y, map[y][x])
//                    y = min(mapHeight - 1, y + 1)
                    inventoryState.handleInput(StateProperties.inventoryItemDown)
                }
                KeyType.ArrowLeft -> {
//                    mapView.set(x, y, map[y][x])
//                    x = max(0, x - 1)
                }
                KeyType.ArrowRight -> {
//                    mapView.set(x, y, map[y][x])
//                    x = min(mapWidth - 1, x + 1)
                }
                KeyType.Enter -> {
                    inventoryState.handleInput(StateProperties.inventoryItemAction)
                }
            }
        }
    }
}

val map = (1..30).map {
    (1..60).map { Cell(GroundType.Fire, mutableListOf(), null) }.toMutableList()
}

fun setup(mapView: MapView) {
    Cell::class.java.getResourceAsStream("/level1.txt")!!.bufferedReader().lineSequence()
        .filter { it.isNotBlank() }
        .withIndex()
        .forEach {
            val y = it.index
            for ((x, c) in it.value.withIndex()) {
                val cell = Cell(c.groundType(), mutableListOf(), null)
                map[y][x] = cell
                mapView.set(x, y, cell)
            }
        }
}

fun Char.groundType(): GroundType = when (this) {
    '.' -> GroundType.Land
    '~' -> GroundType.Water
    '@' -> GroundType.Fire
    '*' -> GroundType.Stone
    '!' -> GroundType.LevelEnd
    else -> throw IllegalStateException("Unknown char")
}
