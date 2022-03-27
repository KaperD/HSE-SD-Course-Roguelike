package ru.hse.roguelike.ui.inventory

import ru.hse.roguelike.model.Hero
import ru.hse.roguelike.model.item.Item
import ru.hse.roguelike.ui.Color
import ru.hse.roguelike.ui.Drawable
import ru.hse.roguelike.ui.window.GameWindow
import ru.hse.roguelike.utils.drawText

class LanternaInventoryView(
    private val window: GameWindow
) : InventoryView {
    private val image = window.createImage().apply { fill(' ', background = Color.ANSI.BorderColor) }
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
            appendTitle("Hero stats:")
            appendLine("HP = ${hero.health}/${hero.maximumHealth}")
            appendLine("Items count = ${hero.items.size}")
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
        itemsImage.setLine(0, 0, "Items:", foreground = Color.ANSI.TitleColor)
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

    private fun drawItem(item: Item, position: Int, background: Color = Color.ANSI.Default) {
        val line = if (item.isUsed) "${item.name} (used)" else item.name
        itemsImage.setLine(0, position + 1, line, background = background)
    }

    private fun drawSelectedItem(item: Item, position: Int) {
        drawItem(item, position, Color.ANSI.YellowBright)
        drawItemInfo(item)
    }

    private fun drawItemInfo(item: Item) {
        infoImage.drawText {
            appendTitle("Item info:")
            appendText(item.description)
        }
    }
}
