package ru.hse.roguelike.ui.map

import com.googlecode.lanterna.TextCharacter
import com.googlecode.lanterna.graphics.BasicTextImage
import io.mockk.*
import org.junit.jupiter.api.Test
import ru.hse.roguelike.model.Cell
import ru.hse.roguelike.model.GroundType
import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.Hero
import ru.hse.roguelike.model.item.DisposableItem
import ru.hse.roguelike.model.item.ItemType
import ru.hse.roguelike.model.item.ReusableItem
import ru.hse.roguelike.property.ColorProperties
import ru.hse.roguelike.property.StringProperties
import ru.hse.roguelike.property.ViewProperties
import ru.hse.roguelike.ui.Image
import ru.hse.roguelike.ui.window.GameWindow
import kotlin.test.assertEquals

internal class LanternaMapViewTest {

    @Test
    fun `test set cell`() {
        val width = 51
        val height = 51
        val baseImage = Image(BasicTextImage(width, height))
        val image = slot<Image>()
        val window = mockk<GameWindow>()
        every { window.createImage() } returns baseImage
        every { window.show(capture(image)) } answers { }

        val mapView = LanternaMapView(window, 4, 2)
        verify { window.createImage() }
        confirmVerified(window)

        mapView.set(0, 0, Cell(GroundType.Fire, mutableListOf(), null))
        mapView.set(1, 0, Cell(GroundType.Land, mutableListOf(), null))
        mapView.set(2, 0, Cell(GroundType.Stone, mutableListOf(), null))
        mapView.set(3, 0, Cell(GroundType.Stone, mutableListOf(ReusableItem("id", "name", "desc", ItemType.Body)), null))
        mapView.set(0, 1, Cell(GroundType.Water, mutableListOf(), null))
        mapView.set(1, 1, Cell(GroundType.LevelEnd, mutableListOf(), null))
        mapView.set(2, 1, Cell(GroundType.Fire, mutableListOf(ReusableItem("id", "name", "desc", ItemType.Body)), Hero(10, 100, 10, Position(2, 1), mutableListOf())))
        mapView.set(3, 1, Cell(GroundType.LevelEnd, mutableListOf(), Hero(10, 100, 10, Position(2, 1), mutableListOf())))
        mapView.show()
        verify { window.show(baseImage) }
        confirmVerified(window)

        assertEquals(
            TextCharacter.fromCharacter(
                ViewProperties.fireSymbol,
                ColorProperties.fireColor.textColor,
                ColorProperties.defaultColor.textColor
            )[0],
            image.captured.getCharacterAt(0, 0)
        )
        assertEquals(
            TextCharacter.fromCharacter(
                ViewProperties.landSymbol,
                ColorProperties.landColor.textColor,
                ColorProperties.defaultColor.textColor
            )[0],
            image.captured.getCharacterAt(1, 0)
        )
        assertEquals(
            TextCharacter.fromCharacter(
                ViewProperties.stoneSymbol,
                ColorProperties.stoneColor.textColor,
                ColorProperties.defaultColor.textColor
            )[0],
            image.captured.getCharacterAt(2, 0)
        )
        assertEquals(
            TextCharacter.fromCharacter(
                ViewProperties.itemSymbol,
                ColorProperties.itemColor.textColor,
                ColorProperties.defaultColor.textColor
            )[0],
            image.captured.getCharacterAt(3, 0)
        )
        assertEquals(
            TextCharacter.fromCharacter(
                ViewProperties.waterSymbol,
                ColorProperties.waterColor.textColor,
                ColorProperties.defaultColor.textColor
            )[0],
            image.captured.getCharacterAt(0, 1)
        )
        assertEquals(
            TextCharacter.fromCharacter(
                ViewProperties.levelEndSymbol,
                ColorProperties.levelEndColor.textColor,
                ColorProperties.defaultColor.textColor
            )[0],
            image.captured.getCharacterAt(1, 1)
        )
        assertEquals(
            TextCharacter.fromCharacter(
                ViewProperties.heroSymbol,
                ColorProperties.heroColor.textColor,
                ColorProperties.defaultColor.textColor
            )[0],
            image.captured.getCharacterAt(2, 1)
        )
        assertEquals(
            TextCharacter.fromCharacter(
                ViewProperties.heroSymbol,
                ColorProperties.heroColor.textColor,
                ColorProperties.defaultColor.textColor
            )[0],
            image.captured.getCharacterAt(3, 1)
        )
    }

    @Test
    fun `test set highlighted`() {
        val width = 51
        val height = 51
        val baseImage = Image(BasicTextImage(width, height))
        val image = slot<Image>()
        val window = mockk<GameWindow>()
        every { window.createImage() } returns baseImage
        every { window.show(capture(image)) } answers { }

        val mapView = LanternaMapView(window, 4, 2)
        verify { window.createImage() }
        confirmVerified(window)

        mapView.setHighlighted(0, 0, Cell(GroundType.Fire, mutableListOf(), null))
        mapView.setHighlighted(1, 0, Cell(GroundType.Land, mutableListOf(), null))
        mapView.setHighlighted(2, 0, Cell(GroundType.Stone, mutableListOf(), null))
        mapView.setHighlighted(3, 0, Cell(GroundType.Stone, mutableListOf(ReusableItem("id", "name", "desc", ItemType.Body)), null))
        mapView.setHighlighted(0, 1, Cell(GroundType.Water, mutableListOf(), null))
        mapView.setHighlighted(1, 1, Cell(GroundType.LevelEnd, mutableListOf(), null))
        mapView.setHighlighted(2, 1, Cell(GroundType.Fire, mutableListOf(ReusableItem("id", "name", "desc", ItemType.Body)), Hero(10, 100, 10, Position(2, 1), mutableListOf())))
        mapView.setHighlighted(3, 1, Cell(GroundType.LevelEnd, mutableListOf(), Hero(10, 100, 10, Position(2, 1), mutableListOf())))
        mapView.show()
        verify { window.show(baseImage) }
        confirmVerified(window)

        assertEquals(
            TextCharacter.fromCharacter(
                ViewProperties.fireSymbol,
                ColorProperties.fireColor.textColor,
                ColorProperties.highlightColor.textColor
            )[0],
            image.captured.getCharacterAt(0, 0)
        )
        assertEquals(
            TextCharacter.fromCharacter(
                ViewProperties.landSymbol,
                ColorProperties.landColor.textColor,
                ColorProperties.highlightColor.textColor
            )[0],
            image.captured.getCharacterAt(1, 0)
        )
        assertEquals(
            TextCharacter.fromCharacter(
                ViewProperties.stoneSymbol,
                ColorProperties.stoneColor.textColor,
                ColorProperties.highlightColor.textColor
            )[0],
            image.captured.getCharacterAt(2, 0)
        )
        assertEquals(
            TextCharacter.fromCharacter(
                ViewProperties.itemSymbol,
                ColorProperties.itemColor.textColor,
                ColorProperties.highlightColor.textColor
            )[0],
            image.captured.getCharacterAt(3, 0)
        )
        assertEquals(
            TextCharacter.fromCharacter(
                ViewProperties.waterSymbol,
                ColorProperties.waterColor.textColor,
                ColorProperties.highlightColor.textColor
            )[0],
            image.captured.getCharacterAt(0, 1)
        )
        assertEquals(
            TextCharacter.fromCharacter(
                ViewProperties.levelEndSymbol,
                ColorProperties.levelEndColor.textColor,
                ColorProperties.highlightColor.textColor
            )[0],
            image.captured.getCharacterAt(1, 1)
        )
        assertEquals(
            TextCharacter.fromCharacter(
                ViewProperties.heroSymbol,
                ColorProperties.heroColor.textColor,
                ColorProperties.highlightColor.textColor
            )[0],
            image.captured.getCharacterAt(2, 1)
        )
        assertEquals(
            TextCharacter.fromCharacter(
                ViewProperties.heroSymbol,
                ColorProperties.heroColor.textColor,
                ColorProperties.highlightColor.textColor
            )[0],
            image.captured.getCharacterAt(3, 1)
        )
    }

    @Test
    fun `test set cell info`() {
        val width = 51
        val height = 51
        val baseImage = Image(BasicTextImage(width, height))
        val image = slot<Image>()
        val window = mockk<GameWindow>()
        every { window.createImage() } returns baseImage
        every { window.show(capture(image)) } answers { }

        val mapView = LanternaMapView(window, 4, 2)
        verify { window.createImage() }
        confirmVerified(window)

        fun check(groundType: GroundType, hasCreature: Boolean = false) {
            for ((i, c) in StringProperties.cellInfo.withIndex()) {
                assertEquals(
                    TextCharacter.fromCharacter(
                        c,
                        ColorProperties.titleColor.textColor,
                        ColorProperties.defaultColor.textColor
                    )[0],
                    image.captured.getCharacterAt(5 + i, 0)
                )
            }
            for ((i, c) in "${StringProperties.cellType} = $groundType".withIndex()) {
                assertEquals(
                    TextCharacter.fromCharacter(
                        c,
                        ColorProperties.textColor.textColor,
                        ColorProperties.defaultColor.textColor
                    )[0],
                    image.captured.getCharacterAt(5 + i, 1)
                )
            }
            if (hasCreature) {
                for ((i, c) in StringProperties.creatureInfo.withIndex()) {
                    assertEquals(
                        TextCharacter.fromCharacter(
                            c,
                            ColorProperties.titleColor.textColor,
                            ColorProperties.defaultColor.textColor
                        )[0],
                        image.captured.getCharacterAt(5 + i, 2)
                    )
                }
            }
        }

        mapView.setCellInfo(Cell(GroundType.Land, mutableListOf(), null))
        mapView.show()
        check(GroundType.Land)
        mapView.setCellInfo(Cell(GroundType.Fire, mutableListOf(), null))
        mapView.show()
        check(GroundType.Fire)
        mapView.setCellInfo(Cell(GroundType.Water, mutableListOf(), null))
        mapView.show()
        check(GroundType.Water)
        mapView.setCellInfo(Cell(GroundType.Stone, mutableListOf(), null))
        mapView.show()
        check(GroundType.Stone)
        mapView.setCellInfo(Cell(GroundType.LevelEnd, mutableListOf(), null))
        mapView.show()
        check(GroundType.LevelEnd)
        mapView.setCellInfo(Cell(GroundType.Land, mutableListOf(ReusableItem("id", "name", "desc", ItemType.Body)), null))
        mapView.show()
        check(GroundType.Land)
        mapView.setCellInfo(Cell(GroundType.Land, mutableListOf(), Hero(10, 100, 10, Position(0, 0), mutableListOf())))
        mapView.show()
        check(GroundType.Land)
        mapView.setCellInfo(Cell(GroundType.Land, mutableListOf(ReusableItem("id", "name", "desc", ItemType.Body)), Hero(10, 100, 10, Position(0, 0), mutableListOf())))
        mapView.show()
        check(GroundType.Land)
    }

    @Test
    fun `test hero stats`() {
        val width = 51
        val height = 51
        val baseImage = Image(BasicTextImage(width, height))
        val image = slot<Image>()
        val window = mockk<GameWindow>()
        every { window.createImage() } returns baseImage
        every { window.show(capture(image)) } answers { }

        val mapView = LanternaMapView(window, 4, 2)
        verify { window.createImage() }
        confirmVerified(window)

        fun check(hero: Hero) {
            for ((i, c) in StringProperties.heroStats.withIndex()) {
                assertEquals(
                    TextCharacter.fromCharacter(
                        c,
                        ColorProperties.titleColor.textColor,
                        ColorProperties.defaultColor.textColor
                    )[0],
                    image.captured.getCharacterAt(5 + i, 0)
                )
            }
            for ((i, c) in "${StringProperties.health} = ${hero.health}/${hero.maximumHealth}".withIndex()) {
                assertEquals(
                    TextCharacter.fromCharacter(
                        c,
                        ColorProperties.textColor.textColor,
                        ColorProperties.defaultColor.textColor
                    )[0],
                    image.captured.getCharacterAt(5 + i, 1)
                )
            }
            for ((i, c) in "${StringProperties.attackDamage} = ${hero.attackDamage}".withIndex()) {
                assertEquals(
                    TextCharacter.fromCharacter(
                        c,
                        ColorProperties.textColor.textColor,
                        ColorProperties.defaultColor.textColor
                    )[0],
                    image.captured.getCharacterAt(5 + i, 2)
                )
            }
            for ((i, c) in "${StringProperties.itemsCount} = ${hero.items.size}".withIndex()) {
                assertEquals(
                    TextCharacter.fromCharacter(
                        c,
                        ColorProperties.textColor.textColor,
                        ColorProperties.defaultColor.textColor
                    )[0],
                    image.captured.getCharacterAt(5 + i, 3)
                )
            }
        }

        val hero = Hero(10, 100, 10, Position(0, 0), mutableListOf())
        mapView.setHeroStats(hero)
        mapView.show()
        check(hero)

        hero.items.add(DisposableItem("id", "name", "desc"))
        mapView.setHeroStats(hero)
        mapView.show()
        check(hero)
    }
}
