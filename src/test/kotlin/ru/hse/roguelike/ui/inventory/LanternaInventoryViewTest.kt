package ru.hse.roguelike.ui.inventory

import com.googlecode.lanterna.TextCharacter
import com.googlecode.lanterna.graphics.BasicTextImage
import io.mockk.*
import org.junit.jupiter.api.Test
import ru.hse.roguelike.factory.ItemFactoryImpl
import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.Hero
import ru.hse.roguelike.property.ColorProperties
import ru.hse.roguelike.property.StringProperties
import ru.hse.roguelike.ui.Image
import ru.hse.roguelike.ui.window.GameWindow
import kotlin.test.assertEquals

internal class LanternaInventoryViewTest {

    @Test
    fun `test set items`() {
        val width = 51
        val height = 51
        val baseImage = Image(BasicTextImage(width, height))
        val image = slot<Image>()
        val window = mockk<GameWindow>()
        every { window.createImage() } returns baseImage
        every { window.show(capture(image)) } answers { }

        val inventoryView = LanternaInventoryView(window)
        verify { window.createImage() }
        confirmVerified(window)
        val itemFactory = ItemFactoryImpl()
        val item1 = itemFactory.getById("platemail")
        val item2 = itemFactory.getById("tango")
        inventoryView.setItems(listOf(item1, item2), 0)
        inventoryView.show()
        verify { window.show(baseImage) }
        confirmVerified(window)

        for ((i, c) in StringProperties.items.withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(
                    c,
                    ColorProperties.titleColor.textColor,
                    ColorProperties.defaultColor.textColor
                )[0],
                image.captured.getCharacterAt(i, 0)
            )
        }
        for ((i, c) in "Platemail".withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(
                    c,
                    ColorProperties.textColor.textColor,
                    ColorProperties.highlightColor.textColor
                )[0],
                image.captured.getCharacterAt(i, 1)
            )
        }
        for ((i, c) in "Tango".withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(
                    c,
                    ColorProperties.textColor.textColor,
                    ColorProperties.defaultColor.textColor
                )[0],
                image.captured.getCharacterAt(i, 2)
            )
        }


        for ((i, c) in StringProperties.itemInfo.withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(
                    c,
                    ColorProperties.titleColor.textColor,
                    ColorProperties.defaultColor.textColor
                )[0],
                image.captured.getCharacterAt(26 + i, 26)
            )
        }
        for ((i, c) in "${StringProperties.itemType} = ${item1.itemType}".withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(
                    c,
                    ColorProperties.textColor.textColor,
                    ColorProperties.defaultColor.textColor
                )[0],
                image.captured.getCharacterAt(26 + i, 27)
            )
        }
    }

    @Test
    fun `test set chosen item`() {
        val width = 101
        val height = 51
        val baseImage = Image(BasicTextImage(width, height))
        val image = slot<Image>()
        val window = mockk<GameWindow>()
        every { window.createImage() } returns baseImage
        every { window.show(capture(image)) } answers { }

        val inventoryView = LanternaInventoryView(window)
        verify { window.createImage() }
        confirmVerified(window)
        val itemFactory = ItemFactoryImpl()
        val item1 = itemFactory.getById("javelin")
        val item2 = itemFactory.getById("tango")
        item2.apply(Hero(10, 100, 10, Position(0, 0), mutableListOf(item2)))
        inventoryView.setItems(listOf(item1, item2), 0)
        inventoryView.show()
        verify { window.show(baseImage) }
        confirmVerified(window)

        inventoryView.setChosenItem(1)

        for ((i, c) in StringProperties.items.withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(
                    c,
                    ColorProperties.titleColor.textColor,
                    ColorProperties.defaultColor.textColor
                )[0],
                image.captured.getCharacterAt(i, 0)
            )
        }
        for ((i, c) in "Javelin".withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(
                    c,
                    ColorProperties.textColor.textColor,
                    ColorProperties.defaultColor.textColor
                )[0],
                image.captured.getCharacterAt(i, 1)
            )
        }
        for ((i, c) in "Tango (${StringProperties.used})".withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(
                    c,
                    ColorProperties.textColor.textColor,
                    ColorProperties.highlightColor.textColor
                )[0],
                image.captured.getCharacterAt(i, 2)
            )
        }


        for ((i, c) in StringProperties.itemInfo.withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(
                    c,
                    ColorProperties.titleColor.textColor,
                    ColorProperties.defaultColor.textColor
                )[0],
                image.captured.getCharacterAt(51 + i, 26)
            )
        }
        for ((i, c) in "${StringProperties.itemType} = ${item2.itemType}".withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(
                    c,
                    ColorProperties.textColor.textColor,
                    ColorProperties.defaultColor.textColor
                )[0],
                image.captured.getCharacterAt(51 + i, 27)
            )
        }
    }

    @Test
    fun `test set hero stats`() {
        val width = 51
        val height = 51
        val baseImage = Image(BasicTextImage(width, height))
        val image = slot<Image>()
        val window = mockk<GameWindow>()
        every { window.createImage() } returns baseImage
        every { window.show(capture(image)) } answers { }

        val inventoryView = LanternaInventoryView(window)
        val hero = Hero(90, 100, 10, Position(0, 0), mutableListOf())
        inventoryView.setHeroStats(hero)
        inventoryView.show()

        for ((i, c) in StringProperties.heroStats.withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(
                    c,
                    ColorProperties.titleColor.textColor,
                    ColorProperties.defaultColor.textColor
                )[0],
                image.captured.getCharacterAt(26 + i, 0)
            )
        }
        for ((i, c) in "${StringProperties.health} = ${hero.health}/${hero.maximumHealth}".withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(
                    c,
                    ColorProperties.textColor.textColor,
                    ColorProperties.defaultColor.textColor
                )[0],
                image.captured.getCharacterAt(26 + i, 1)
            )
        }
    }

    @Test
    fun `test empty items`() {
        val width = 51
        val height = 51
        val baseImage = Image(BasicTextImage(width, height))
        val image = slot<Image>()
        val window = mockk<GameWindow>()
        every { window.createImage() } returns baseImage
        every { window.show(capture(image)) } answers { }

        val inventoryView = LanternaInventoryView(window)
        verify { window.createImage() }
        confirmVerified(window)
        inventoryView.setItems(emptyList(), 0)
        inventoryView.show()

        for ((i, c) in StringProperties.items.withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(
                    c,
                    ColorProperties.titleColor.textColor,
                    ColorProperties.defaultColor.textColor
                )[0],
                image.captured.getCharacterAt(i, 0)
            )
        }
        for ((i, c) in StringProperties.itemInfo.withIndex()) {
            assertEquals(
                TextCharacter.fromCharacter(
                    c,
                    ColorProperties.titleColor.textColor,
                    ColorProperties.defaultColor.textColor
                )[0],
                image.captured.getCharacterAt(26 + i, 26)
            )
        }
    }
}
