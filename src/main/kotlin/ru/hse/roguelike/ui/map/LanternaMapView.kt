package ru.hse.roguelike.ui.map

import ru.hse.roguelike.model.Cell
import ru.hse.roguelike.model.Creature
import ru.hse.roguelike.model.GroundType
import ru.hse.roguelike.model.GroundType.*
import ru.hse.roguelike.model.Hero
import ru.hse.roguelike.ui.Color
import ru.hse.roguelike.ui.window.GameWindow
import ru.hse.roguelike.utils.drawText

class LanternaMapView(
    private val window: GameWindow,
    mapWidth: Int,
    mapHeight: Int
) : MapView {
    private val image = window.createImage().apply { fill(' ', background = Color.ANSI.BorderColor) }
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
        mapImage.set(x, y, symbol, foreground = foreground, background = Color.ANSI.YellowBright)
    }

    override fun setHeroStats(hero: Hero) {
        infoImage.drawText {
            appendTitle("Hero stats:")
            appendLine("HP = ${hero.health}")
        }
    }

    override fun setCellInfo(cell: Cell) {
        infoImage.drawText {
            appendTitle("Cell info:")
            appendLine("Cell type = ${cell.groundType.name}")
            appendLine("Items count = ${cell.items.size}")
            cell.creature?.let { creature ->
                appendTitle("Creature info:")
                appendText(creature.info())
            }
        }
    }

    override fun show() {
        window.show(image)
    }

    private fun Creature.info(): String = when (this) {
        is Hero -> """
            Type = hero
            HP = $health
        """.trimIndent()
        else -> throw IllegalStateException("Unknown creature type")
    }

    private fun Cell.representation(): Pair<Char, Color> = when {
        creature != null -> when (creature) {
            is Hero -> '#' to Color.ANSI.Green
            else -> throw IllegalStateException("Unknown creature type")
        }
        items.isNotEmpty() -> '?' to Color.ANSI.GreenBright
        else -> groundType.representation()
    }

    private fun GroundType.representation(): Pair<Char, Color> = when (this) {
        Land -> '.' to Color.ANSI.Black
        Water -> '~' to Color.ANSI.Blue
        Fire -> '@' to Color.ANSI.Red
        Stone -> '*' to Color.ANSI.Yellow
        LevelEnd -> '!' to Color.ANSI.Green
    }
}
