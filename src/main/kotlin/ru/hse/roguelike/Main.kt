package ru.hse.roguelike

import com.googlecode.lanterna.input.KeyType
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame
import ru.hse.roguelike.model.Cell
import ru.hse.roguelike.model.GroundType
import ru.hse.roguelike.property.Properties
import ru.hse.roguelike.ui.map.LanternaMapView
import ru.hse.roguelike.ui.map.MapView
import ru.hse.roguelike.ui.window.LanternaGameWindow
import javax.swing.JFrame
import kotlin.math.max
import kotlin.math.min

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
        while (true) {
            mapView.setHighlighted(x, y, map[y][x])
            mapView.setCellInfo(map[y][x])
            mapView.show()
            when (terminal.readInput().keyType) {
                KeyType.Escape, KeyType.EOF -> break
                KeyType.ArrowUp -> {
                    mapView.set(x, y, map[y][x])
                    y = max(0, y - 1)
                }
                KeyType.ArrowDown -> {
                    mapView.set(x, y, map[y][x])
                    y = min(mapHeight - 1, y + 1)
                }
                KeyType.ArrowLeft -> {
                    mapView.set(x, y, map[y][x])
                    x = max(0, x - 1)
                }
                KeyType.ArrowRight -> {
                    mapView.set(x, y, map[y][x])
                    x = min(mapWidth - 1, x + 1)
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
