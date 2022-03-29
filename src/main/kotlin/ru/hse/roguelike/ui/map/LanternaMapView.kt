package ru.hse.roguelike.ui.map

import ru.hse.roguelike.model.Cell
import ru.hse.roguelike.model.GroundType
import ru.hse.roguelike.model.GroundType.*
import ru.hse.roguelike.model.creature.Creature
import ru.hse.roguelike.model.creature.Hero
import ru.hse.roguelike.property.ColorProperties.borderColor
import ru.hse.roguelike.property.ColorProperties.fireColor
import ru.hse.roguelike.property.ColorProperties.heroColor
import ru.hse.roguelike.property.ColorProperties.highlightColor
import ru.hse.roguelike.property.ColorProperties.itemColor
import ru.hse.roguelike.property.ColorProperties.landColor
import ru.hse.roguelike.property.ColorProperties.levelEndColor
import ru.hse.roguelike.property.ColorProperties.stoneColor
import ru.hse.roguelike.property.ColorProperties.waterColor
import ru.hse.roguelike.property.StringProperties
import ru.hse.roguelike.property.StringProperties.cellInfo
import ru.hse.roguelike.property.StringProperties.cellType
import ru.hse.roguelike.property.StringProperties.creatureInfo
import ru.hse.roguelike.property.StringProperties.health
import ru.hse.roguelike.property.StringProperties.hero
import ru.hse.roguelike.property.StringProperties.heroStats
import ru.hse.roguelike.property.StringProperties.itemsCount
import ru.hse.roguelike.property.StringProperties.type
import ru.hse.roguelike.property.ViewProperties.fireSymbol
import ru.hse.roguelike.property.ViewProperties.heroSymbol
import ru.hse.roguelike.property.ViewProperties.itemSymbol
import ru.hse.roguelike.property.ViewProperties.landSymbol
import ru.hse.roguelike.property.ViewProperties.levelEndSymbol
import ru.hse.roguelike.property.ViewProperties.stoneSymbol
import ru.hse.roguelike.property.ViewProperties.waterSymbol
import ru.hse.roguelike.ui.Color
import ru.hse.roguelike.ui.window.GameWindow
import ru.hse.roguelike.utils.drawText

class LanternaMapView(
    private val window: GameWindow,
    mapWidth: Int,
    mapHeight: Int
) : MapView {
    private val image = window.createImage().apply { fill(' ', background = borderColor) }
    private val mapImage = image.subImage(0, 0, mapWidth, mapHeight).apply { clear() }
    private val infoImage = image.subImage(
        mapWidth + 1,
        0,
        image.width - (mapWidth + 1),
        image.height
    ).apply { clear() }

    override fun set(x: Int, y: Int, cell: Cell) {
        val (symbol, foreground) = cell.representation()
        mapImage.set(x, y, symbol, foreground = foreground)
    }

    override fun setHighlighted(x: Int, y: Int, cell: Cell) {
        val (symbol, foreground) = cell.representation()
        mapImage.set(x, y, symbol, foreground = foreground, background = highlightColor)
    }

    override fun setHeroStats(hero: Hero) {
        infoImage.drawText {
            appendTitle("$heroStats:")
            appendLine("$health = ${hero.health}/${hero.maximumHealth}")
            appendLine("$itemsCount = ${hero.items.size}")
        }
    }

    override fun setCellInfo(cell: Cell) {
        infoImage.drawText {
            appendTitle("$cellInfo:")
            appendLine("$cellType = ${cell.groundType.name}")
            if (cell.items.size > 0) {
                appendLine("$itemsCount = ${cell.items.size}")
            }
            cell.creature?.let { creature ->
                appendTitle("$creatureInfo:")
                appendText(creature.info())
            }
        }
    }

    override fun show() {
        window.show(image)
    }

    private fun Creature.info(): String = when (this) {
        is Hero -> """
            $type = $hero
            ${StringProperties.health} = $health/$maximumHealth
        """.trimIndent()
        else -> throw IllegalStateException("Unknown creature type")
    }

    private fun Cell.representation(): Pair<Char, Color> = when {
        creature != null -> when (creature) {
            is Hero -> heroSymbol to heroColor
            else -> throw IllegalStateException("Unknown creature type")
        }
        items.isNotEmpty() -> itemSymbol to itemColor
        else -> groundType.representation()
    }

    private fun GroundType.representation(): Pair<Char, Color> = when (this) {
        Land -> landSymbol to landColor
        Water -> waterSymbol to waterColor
        Fire -> fireSymbol to fireColor
        Stone -> stoneSymbol to stoneColor
        LevelEnd -> levelEndSymbol to levelEndColor
    }
}
