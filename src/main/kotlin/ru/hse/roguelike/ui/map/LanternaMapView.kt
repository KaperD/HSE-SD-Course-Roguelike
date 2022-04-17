package ru.hse.roguelike.ui.map

import ru.hse.roguelike.model.Cell
import ru.hse.roguelike.model.GroundType
import ru.hse.roguelike.model.GroundType.*
import ru.hse.roguelike.model.creature.*
import ru.hse.roguelike.model.creature.mob.*
import ru.hse.roguelike.property.ColorProperties
import ru.hse.roguelike.property.ColorProperties.aggressiveMobColor
import ru.hse.roguelike.property.ColorProperties.borderColor
import ru.hse.roguelike.property.ColorProperties.cowardMobColor
import ru.hse.roguelike.property.ColorProperties.fireColor
import ru.hse.roguelike.property.ColorProperties.grassColor
import ru.hse.roguelike.property.ColorProperties.heroColor
import ru.hse.roguelike.property.ColorProperties.highlightColor
import ru.hse.roguelike.property.ColorProperties.itemColor
import ru.hse.roguelike.property.ColorProperties.landColor
import ru.hse.roguelike.property.ColorProperties.levelEndColor
import ru.hse.roguelike.property.ColorProperties.passiveMobColor
import ru.hse.roguelike.property.ColorProperties.stoneColor
import ru.hse.roguelike.property.ColorProperties.waterColor
import ru.hse.roguelike.property.StringProperties
import ru.hse.roguelike.property.StringProperties.attackDamage
import ru.hse.roguelike.property.StringProperties.cellInfo
import ru.hse.roguelike.property.StringProperties.cellType
import ru.hse.roguelike.property.StringProperties.creatureInfo
import ru.hse.roguelike.property.StringProperties.health
import ru.hse.roguelike.property.StringProperties.hero
import ru.hse.roguelike.property.StringProperties.heroStats
import ru.hse.roguelike.property.StringProperties.itemsCount
import ru.hse.roguelike.property.StringProperties.type
import ru.hse.roguelike.property.ViewProperties
import ru.hse.roguelike.property.ViewProperties.aggressiveMobSymbol
import ru.hse.roguelike.property.ViewProperties.cowardMobSymbol
import ru.hse.roguelike.property.ViewProperties.fireSymbol
import ru.hse.roguelike.property.ViewProperties.grassSymbol
import ru.hse.roguelike.property.ViewProperties.heroSymbol
import ru.hse.roguelike.property.ViewProperties.itemSymbol
import ru.hse.roguelike.property.ViewProperties.landSymbol
import ru.hse.roguelike.property.ViewProperties.levelEndSymbol
import ru.hse.roguelike.property.ViewProperties.passiveMobSymbol
import ru.hse.roguelike.property.ViewProperties.stoneSymbol
import ru.hse.roguelike.property.ViewProperties.waterSymbol
import ru.hse.roguelike.ui.Color
import ru.hse.roguelike.ui.window.GameWindow
import ru.hse.roguelike.utils.drawText

/**
 * Реализация отображения карты
 * с помощью библиотеки Lanterna
 * @param window Окно для отображения данного view
 * @param mapWidth  Ширина карты
 * @param mapHeight Высота карты
 */
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
            appendLine("$attackDamage = ${hero.attackDamage}")
            appendLine("$itemsCount = ${hero.items.size}")
            appendLine("${StringProperties.level} = ${hero.level}")
            appendLine("${StringProperties.experienceForNextLevel} = ${hero.experienceForNextLevel}")
        }
    }

    override fun setCellInfo(cell: Cell) {
        infoImage.drawText {
            appendTitle("$cellInfo:")
            appendLine("$cellType = ${cell.groundType}")
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
            ${StringProperties.attackDamage} = $attackDamage
            $itemsCount = ${items.size}
            ${StringProperties.level} = $level
        """.trimIndent()
        is Mob -> """
            $type = ${type()}
            ${StringProperties.health} = $health/$maximumHealth
            ${StringProperties.attackDamage} = $attackDamage
        """.trimIndent()
        else -> throw IllegalStateException("Unknown creature type")
    }

    private fun Mob.type(): String = when (mobType) {
        MobType.Coward -> StringProperties.coward
        MobType.Aggressive -> StringProperties.aggressive
        MobType.Passive -> StringProperties.passive
    }

    private fun Cell.representation(): Pair<Char, Color> = when {
        creature != null -> when (val creature = creature) {
            is Hero -> heroSymbol to heroColor
            is Mob -> when (creature.mobType) {
                MobType.Coward -> cowardMobSymbol to cowardMobColor
                MobType.Aggressive -> aggressiveMobSymbol to aggressiveMobColor
                MobType.Passive -> passiveMobSymbol to passiveMobColor
            }
            else -> throw IllegalStateException("Unknown creature type")
        }
        items.isNotEmpty() -> itemSymbol to itemColor
        else -> groundType.representation()
    }

    private fun GroundType.representation(): Pair<Char, Color> = when (this) {
        Land -> landSymbol to landColor
        Grass -> grassSymbol to grassColor
        Water -> waterSymbol to waterColor
        Fire -> fireSymbol to fireColor
        Stone -> stoneSymbol to stoneColor
        LevelEnd -> levelEndSymbol to levelEndColor
    }
}
