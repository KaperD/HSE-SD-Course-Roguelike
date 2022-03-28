package ru.hse.roguelike.ui.inventory

import ru.hse.roguelike.model.creature.Hero
import ru.hse.roguelike.model.item.Item
import ru.hse.roguelike.property.ColorProperties.borderColor
import ru.hse.roguelike.property.ColorProperties.defaultColor
import ru.hse.roguelike.property.ColorProperties.highlightColor
import ru.hse.roguelike.property.ColorProperties.titleColor
import ru.hse.roguelike.property.StringProperties
import ru.hse.roguelike.property.StringProperties.health
import ru.hse.roguelike.property.StringProperties.heroStats
import ru.hse.roguelike.property.StringProperties.itemInfo
import ru.hse.roguelike.property.StringProperties.itemsCount
import ru.hse.roguelike.property.StringProperties.used
import ru.hse.roguelike.ui.Color
import ru.hse.roguelike.ui.Drawable
import ru.hse.roguelike.ui.window.GameWindow
import ru.hse.roguelike.utils.drawText

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
            appendLine("$itemsCount = ${hero.items.size}")
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
        itemsImage.clear()
        itemsImage.setLine(0, 0, "${StringProperties.items}:", foreground = titleColor)
        for ((i, item) in items.withIndex()) {
            if (i == chosenPosition) {
                drawSelectedItem(item, i)
            } else {
                drawItem(item, i)
            }
        }
        if (items.isEmpty()) {
            infoImage.clear()
        }
    }

    private fun drawItem(item: Item, position: Int, background: Color = defaultColor) {
        val line = if (item.isUsed) "${item.name} ($used)" else item.name
        itemsImage.setLine(0, position + 1, line, background = background)
    }

    private fun drawSelectedItem(item: Item, position: Int) {
        drawItem(item, position, highlightColor)
        drawItemInfo(item)
    }

    private fun drawItemInfo(item: Item) {
        infoImage.drawText {
            appendTitle("$itemInfo:")
            appendText(item.description)
        }
    }
}
