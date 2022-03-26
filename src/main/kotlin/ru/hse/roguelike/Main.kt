package ru.hse.roguelike

import com.googlecode.lanterna.input.KeyType
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame
import ru.hse.roguelike.model.Cell
import ru.hse.roguelike.model.GroundType
import ru.hse.roguelike.model.Hero
import ru.hse.roguelike.model.Position
import ru.hse.roguelike.ui.LanternaGameWindow
import ru.hse.roguelike.ui.map.LanternaMapView
import java.util.Properties
import javax.swing.JFrame
import kotlin.random.Random

fun main() {
    val properties = Properties().apply {
        load(Cell::class.java.getResourceAsStream("/application.properties"))
    }
    val mapWidth = properties.getProperty("map.width").toInt()
    val mapHeight = properties.getProperty("map.height").toInt()
    val imageWidth = properties.getProperty("image.width").toInt()
    val imageHeight = properties.getProperty("image.height").toInt()
    val factory = DefaultTerminalFactory()
    factory.createTerminal().use { terminal ->
        if (terminal is SwingTerminalFrame) {
            terminal.extendedState = JFrame.MAXIMIZED_BOTH
        }
        terminal.enterPrivateMode()
        val window = LanternaGameWindow(terminal, imageWidth, imageHeight)
        val mapView = LanternaMapView(window, mapWidth, mapHeight)
        do {
            val x = Random.nextInt(mapWidth)
            val y = Random.nextInt(mapHeight)
            val cell = Cell(GroundType.Fire, mutableListOf(), Hero(Random.nextInt(), Position(x, y), mutableListOf()))
            mapView.set(x, y, cell)
            mapView.setCellInfo(cell)
            mapView.show()
            val input = terminal.readInput()
        } while (input.keyType != KeyType.Escape)
    }
}
