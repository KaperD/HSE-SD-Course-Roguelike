package ru.hse.roguelike.ui.inventory

import ru.hse.roguelike.model.Hero
import ru.hse.roguelike.model.Item
import ru.hse.roguelike.ui.Color
import ru.hse.roguelike.ui.Drawable
import ru.hse.roguelike.ui.window.GameWindow

class LanternaInventoryView(
    private val window: GameWindow
) : InventoryView {
    private val image = window.createImage()
    private val itemsImage: Drawable
    private val heroStatsImage: Drawable
    private val infoImage: Drawable
    private val titleColor = Color.ANSI.Blue

    init {
        val itemsImageWidth = image.width / 2
        itemsImage = image.subImage(0, 0, itemsImageWidth, image.height)
        val heroStatsImageHeight = image.height / 2
        heroStatsImage = image.subImage(
            itemsImageWidth + 1,
            0,
            image.width - (itemsImageWidth + 1),
            heroStatsImageHeight
        )
        infoImage = image.subImage(
            itemsImageWidth + 1,
            heroStatsImageHeight + 1,
            image.width - (itemsImageWidth + 1),
            image.height - (heroStatsImageHeight + 1)
        )
        for (y in 0 until image.height) {
            image.set(itemsImageWidth, y, ' ', background = Color.ANSI.White)
        }
        for (x in itemsImageWidth until image.width) {
            image.set(x, heroStatsImageHeight, ' ', background = Color.ANSI.White)
        }
    }

    private var items: List<Item> = emptyList()
    private var chosenPosition = 0

    override fun setItems(items: List<Item>, chosenPosition: Int) {
        this.items = items
        this.chosenPosition = chosenPosition
        drawItems()
    }

    override fun setHeroStats(hero: Hero) {
        heroStatsImage.clear()
        heroStatsImage.setLine(0, 0, "Hero stats:", foreground = titleColor)
        heroStatsImage.setText(
            0, 1,
            """
            HP = ${hero.health}
            Items count = ${hero.items.size}
            """.trimIndent()
        )
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
        itemsImage.setLine(0, 0, "Items:", foreground = titleColor)
        for ((i, item) in items.withIndex()) {
            if (i == chosenPosition) {
                drawSelectedItem(item, i)
            } else {
                drawItem(item, i)
            }
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
        infoImage.clear()
        infoImage.setLine(0, 0, "Item info:", foreground = titleColor)
        infoImage.setText(0, 1, item.description)
    }
}
