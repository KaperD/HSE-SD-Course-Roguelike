package ru.hse.roguelike.ui.inventory

import ru.hse.roguelike.model.creature.Hero
import ru.hse.roguelike.model.item.Item
import ru.hse.roguelike.property.ColorProperties.borderColor
import ru.hse.roguelike.property.ColorProperties.defaultColor
import ru.hse.roguelike.property.ColorProperties.highlightColor
import ru.hse.roguelike.property.StringProperties
import ru.hse.roguelike.property.StringProperties.attackDamage
import ru.hse.roguelike.property.StringProperties.bonusAttackDamage
import ru.hse.roguelike.property.StringProperties.bonusHealth
import ru.hse.roguelike.property.StringProperties.bonusMaximumHealth
import ru.hse.roguelike.property.StringProperties.health
import ru.hse.roguelike.property.StringProperties.heroStats
import ru.hse.roguelike.property.StringProperties.itemInfo
import ru.hse.roguelike.property.StringProperties.itemType
import ru.hse.roguelike.property.StringProperties.itemsCount
import ru.hse.roguelike.property.StringProperties.used
import ru.hse.roguelike.ui.Color
import ru.hse.roguelike.ui.Drawable
import ru.hse.roguelike.ui.window.GameWindow
import ru.hse.roguelike.utils.DrawContext
import ru.hse.roguelike.utils.drawText

/**
 * Реализация отображения инвентаря
 * с помощью библиотеки Lanterna
 * @param window Окно для отображения данного view
 */
class LanternaInventoryView(
    private val window: GameWindow
) : InventoryView {
    private val image = window.createImage().apply { fill(' ', background = borderColor) }
    private val itemsImage: Drawable
    private val heroStatsImage: Drawable
    private val infoImage: Drawable

    init {
        val itemsImageWidth = image.width / 2
        itemsImage = image.subImage(0, 0, itemsImageWidth, image.height).apply { clear() }
        val heroStatsImageHeight = image.height / 2
        heroStatsImage = image.subImage(
            itemsImageWidth + 1,
            0,
            image.width - (itemsImageWidth + 1),
            heroStatsImageHeight
        ).apply { clear() }
        infoImage = image.subImage(
            itemsImageWidth + 1,
            heroStatsImageHeight + 1,
            image.width - (itemsImageWidth + 1),
            image.height - (heroStatsImageHeight + 1)
        ).apply { clear() }
    }

    private var items: List<Item> = emptyList()
    private var chosenPosition = 0

    override fun setItems(items: List<Item>, chosenPosition: Int) {
        this.items = items
        this.chosenPosition = chosenPosition
        drawItems()
    }

    override fun setHeroStats(hero: Hero) {
        heroStatsImage.drawText {
            appendTitle("$heroStats:")
            appendLine("$health = ${hero.health}/${hero.maximumHealth}")
            appendLine("$attackDamage = ${hero.attackDamage}")
            appendLine("$itemsCount = ${hero.items.size}")
            appendLine("${StringProperties.level} = ${hero.level}")
            appendLine("${StringProperties.experienceForNextLevel} = ${hero.experienceForNextLevel}")
        }
    }

    override fun setChosenItem(position: Int) {
        chosenPosition = position
        drawItems()
    }

    override fun show() {
        window.show(image)
    }

    private fun drawItems() {
        itemsImage.drawText {
            appendTitle("${StringProperties.items}:")
            for ((i, item) in items.withIndex()) {
                if (i == chosenPosition) {
                    drawSelectedItem(item, this)
                } else {
                    drawItem(item, this)
                }
            }
        }
        if (items.isEmpty()) {
            infoImage.drawText {
                appendTitle("$itemInfo:")
            }
        }
    }

    private fun drawItem(item: Item, drawContext: DrawContext, background: Color = defaultColor) {
        val line = if (item.isUsed) "${item.name} ($used)" else item.name
        drawContext.appendLine(line, background = background)
    }

    private fun drawSelectedItem(item: Item, drawContext: DrawContext) {
        drawItem(item, drawContext, highlightColor)
        drawItemInfo(item)
    }

    private fun drawItemInfo(item: Item) {
        infoImage.drawText {
            appendTitle("$itemInfo:")
            appendLine("$itemType = ${item.itemType}")
            if (item.healthChange != 0) {
                appendLine("$bonusHealth = ${item.healthChange}")
            }
            if (item.maximumHealthChange != 0) {
                appendLine("$bonusMaximumHealth = ${item.maximumHealthChange}")
            }
            if (item.attackDamageChange != 0) {
                appendLine("$bonusAttackDamage = ${item.attackDamageChange}")
            }
            appendLine("")
            appendText(item.description)
        }
    }
}
