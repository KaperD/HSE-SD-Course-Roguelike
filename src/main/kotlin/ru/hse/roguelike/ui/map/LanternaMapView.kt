package ru.hse.roguelike.ui.map

import ru.hse.roguelike.model.Cell
import ru.hse.roguelike.model.Creature
import ru.hse.roguelike.model.GroundType
import ru.hse.roguelike.model.GroundType.*
import ru.hse.roguelike.model.Hero
import ru.hse.roguelike.ui.Color
import ru.hse.roguelike.ui.GameWindow

class LanternaMapView(
    private val window: GameWindow,
    mapWidth: Int,
    mapHeight: Int
) : MapView {
    private val image = window.createImage()
    private val mapImage = image.subImage(0, 0, mapWidth, mapHeight)
    private val infoImage = image.subImage(
        mapWidth + 1,
        0,
        image.width - (mapWidth + 1),
        image.height
    )

    init {
        for (y in 0 until image.height) {
            image.set(mapWidth, y, ' ', background = Color.ANSI.Blue)
        }
    }

    override fun set(x: Int, y: Int, cell: Cell) {
        val (symbol, foreground) = cell.representation()
        mapImage.set(x, y, symbol, foreground = foreground)
    }

    override fun setHighlighted(x: Int, y: Int, cell: Cell) {
        val (symbol, foreground) = cell.representation()
        mapImage.set(x, y, symbol, foreground = foreground, background = Color.ANSI.YellowBright)
    }

    override fun setHeroStats(hero: Hero) {
        infoImage.setLine(0, 0, "Hero stats:")
        infoImage.setLine(0, 1, "HP = ${hero.health}")
    }

    override fun setCellInfo(cell: Cell) {
        val text = buildString {
            appendLine("Cell info:")
            appendLine("Cell type = ${cell.groundType.name}")
            appendLine("Items count = ${cell.items.size}")
            if (cell.creature != null) {
                appendLine("Creature info:")
                appendLine(cell.creature.info())
            }
        }
        infoImage.setText(0, 0, text)
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
        Land -> '.' to Color.ANSI.Cyan
        Water -> '~' to Color.ANSI.Blue
        Fire -> '$' to Color.ANSI.Red
        Stone -> '*' to Color.ANSI.BlackBright
        LevelEnd -> '!' to Color.ANSI.Green
    }
}
