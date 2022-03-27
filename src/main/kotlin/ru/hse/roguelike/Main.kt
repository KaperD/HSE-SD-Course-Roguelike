package ru.hse.roguelike

import com.googlecode.lanterna.input.KeyType
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame
import ru.hse.roguelike.model.Cell
import ru.hse.roguelike.model.GroundType
import ru.hse.roguelike.model.Hero
import ru.hse.roguelike.model.Position
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
        val hero = Hero(100, 100, Position(0, 0), mutableListOf())
        setup(mapView)
        map[0][0].creature = hero
        mapView.set(0, 0, map[0][0])
        var x = 0
        var y = 0
        while (true) {
            mapView.show()
            when (terminal.readInput().keyType) {
                KeyType.Escape, KeyType.EOF -> break
                KeyType.ArrowUp -> {
                    map[y][x].creature = null
                    mapView.set(x, y, map[y][x])
                    y = max(0, y - 1)
                    map[y][x].creature = hero
                    mapView.set(x, y, map[y][x])
                }
                KeyType.ArrowDown -> {
                    map[y][x].creature = null
                    mapView.set(x, y, map[y][x])
                    y = min(mapHeight - 1, y + 1)
                    map[y][x].creature = hero
                    mapView.set(x, y, map[y][x])
                }
                KeyType.ArrowLeft -> {
                    map[y][x].creature = null
                    mapView.set(x, y, map[y][x])
                    x = max(0, x - 1)
                    map[y][x].creature = hero
                    mapView.set(x, y, map[y][x])
                }
                KeyType.ArrowRight -> {
                    map[y][x].creature = null
                    mapView.set(x, y, map[y][x])
                    x = min(mapWidth - 1, x + 1)
                    map[y][x].creature = hero
                    mapView.set(x, y, map[y][x])
                }
            }
        }
    }
}

val map = (1..30).map {
    (1..60).map { Cell(GroundType.Fire, mutableListOf(), null) }.toMutableList()
}

fun setup(mapView: MapView) {
    Cell::class.java.getResourceAsStream("/level1.txt")!!.bufferedReader().lineSequence().withIndex().forEach {
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
    '$' -> GroundType.Fire
    '*' -> GroundType.Stone
    '!' -> GroundType.LevelEnd
    else -> throw IllegalStateException("Unknown char")
}
