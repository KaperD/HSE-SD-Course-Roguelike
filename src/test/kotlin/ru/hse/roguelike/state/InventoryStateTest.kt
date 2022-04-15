package ru.hse.roguelike.state

import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import ru.hse.roguelike.input.InputType
import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.Hero
import ru.hse.roguelike.model.item.DisposableItem
import ru.hse.roguelike.model.item.ItemType
import ru.hse.roguelike.model.item.ReusableItem
import ru.hse.roguelike.property.StateProperties
import ru.hse.roguelike.sound.GameSound
import ru.hse.roguelike.ui.inventory.InventoryView
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class InventoryStateTest {

    @Test
    fun `test inventory state empty inventory`() {
        val hero = Hero(100, 100, 10, Position(0, 0), mutableListOf())
        val inventoryView = mockk<InventoryView>(relaxed = true)
        val gameSound = mockk<GameSound>(relaxed = true)
        val anotherState = mockk<State>()
        val inventoryState = InventoryState(hero, inventoryView, gameSound, mapOf(InputType.M to anotherState))

        inventoryState.activate()
        verify { inventoryView.setHeroStats(hero) }
        verify { inventoryView.setItems(mutableListOf(), 0) }
        verify { inventoryView.show() }
        confirmVerified(inventoryView)

        assertEquals(inventoryState, inventoryState.handleInput(StateProperties.inventoryItemUp))
        verify { gameSound.beep() }
        confirmVerified(gameSound)
        verify { inventoryView.show() }
        confirmVerified(inventoryView)

        assertEquals(inventoryState, inventoryState.handleInput(StateProperties.inventoryItemDown))
        verify { gameSound.beep() }
        confirmVerified(gameSound)
        verify { inventoryView.show() }
        confirmVerified(inventoryView)

        assertEquals(inventoryState, inventoryState.handleInput(StateProperties.inventoryItemAction))
        verify { gameSound.beep() }
        confirmVerified(gameSound)
        verify { inventoryView.show() }
        confirmVerified(inventoryView)

        assertEquals(inventoryState, inventoryState.handleInput(InputType.I))
        verify { gameSound.beep() }
        confirmVerified(gameSound)
        verify { inventoryView.show() }
        confirmVerified(inventoryView)

        assertEquals(anotherState, inventoryState.handleInput(InputType.M))
        confirmVerified(gameSound)
        confirmVerified(inventoryView)
    }

    @Test
    fun `test inventory state not empty inventory`() {
        val item1 = ReusableItem("reusable", "reusable", "reusable", ItemType.Head)
        val item2 = DisposableItem("disposable", "disposable", "disposable")
        val hero = Hero(100, 100, 10, Position(0, 0), mutableListOf(item1, item2))
        val inventoryView = mockk<InventoryView>(relaxed = true)
        val gameSound = mockk<GameSound>(relaxed = true)
        val inventoryState = InventoryState(hero, inventoryView, gameSound, mapOf())

        inventoryState.activate()
        verify { inventoryView.setHeroStats(hero) }
        verify { inventoryView.setItems(mutableListOf(item1, item2), 0) }
        verify { inventoryView.show() }
        confirmVerified(inventoryView)

        assertEquals(inventoryState, inventoryState.handleInput(StateProperties.inventoryItemUp))
        verify { gameSound.beep() }
        confirmVerified(gameSound)
        verify { inventoryView.show() }
        confirmVerified(inventoryView)

        assertEquals(inventoryState, inventoryState.handleInput(StateProperties.inventoryItemAction))
        confirmVerified(gameSound)
        verify { inventoryView.setHeroStats(hero) }
        verify { inventoryView.setItems(mutableListOf(item1, item2), 0) }
        verify { inventoryView.show() }
        confirmVerified(inventoryView)

        assertEquals(inventoryState, inventoryState.handleInput(StateProperties.inventoryItemDown))
        confirmVerified(gameSound)
        verify { inventoryView.setChosenItem(1) }
        verify { inventoryView.show() }
        confirmVerified(inventoryView)

        assertEquals(inventoryState, inventoryState.handleInput(StateProperties.inventoryItemDown))
        verify { gameSound.beep() }
        confirmVerified(gameSound)
        verify { inventoryView.show() }
        confirmVerified(inventoryView)

        assertEquals(inventoryState, inventoryState.handleInput(StateProperties.inventoryItemAction))
        confirmVerified(gameSound)
        verify { inventoryView.setHeroStats(hero) }
        verify { inventoryView.setItems(mutableListOf(item1), 0) }
        verify { inventoryView.show() }
        confirmVerified(inventoryView)
    }

    @Test
    fun `test inventory state many items`() {
        val item1 = ReusableItem("reusable", "reusable", "reusable", ItemType.Head, 1)
        val item2 = DisposableItem("disposable", "disposable", "disposable", 1)
        val item3 = ReusableItem("reusable3", "reusable3", "reusable3", ItemType.Head, 1)
        val item4 = ReusableItem("reusable4", "reusable4", "reusable4", ItemType.Body, 1)
        val hero = Hero(100, 104, 10, Position(0, 0), mutableListOf(item1, item2, item3, item4))
        val inventoryView = mockk<InventoryView>(relaxed = true)
        val gameSound = mockk<GameSound>(relaxed = true)
        val inventoryState = InventoryState(hero, inventoryView, gameSound, mapOf())

        inventoryState.activate()

        inventoryState.handleInput(StateProperties.inventoryItemAction)
        verify { inventoryView.setHeroStats(hero) }
        verify { inventoryView.setItems(mutableListOf(item1, item2, item3, item4), 0) }
        verify { inventoryView.show() }
        confirmVerified(inventoryView)
        assertEquals(101, hero.health)
        assertEquals(104, hero.maximumHealth)
        assertTrue(item1.isUsed)

        inventoryState.handleInput(StateProperties.inventoryItemDown)
        verify { inventoryView.setChosenItem(1) }
        verify { inventoryView.show() }
        inventoryState.handleInput(StateProperties.inventoryItemAction)
        verify { inventoryView.setHeroStats(hero) }
        verify { inventoryView.setItems(mutableListOf(item1, item3, item4), 1) }
        verify { inventoryView.show() }
        confirmVerified(inventoryView)
        assertEquals(102, hero.health)
        assertEquals(104, hero.maximumHealth)
        assertTrue(item2.isUsed)

        inventoryState.handleInput(StateProperties.inventoryItemAction)
        assertEquals(102, hero.health)
        assertEquals(104, hero.maximumHealth)
        verify { gameSound.beep() }
        confirmVerified(gameSound)
        assertFalse(item3.isUsed)
        verify { inventoryView.show() }
        confirmVerified(inventoryView)

        inventoryState.handleInput(StateProperties.inventoryItemDown)
        verify { inventoryView.setChosenItem(2) }
        verify { inventoryView.show() }
        inventoryState.handleInput(StateProperties.inventoryItemAction)
        verify { inventoryView.setHeroStats(hero) }
        verify { inventoryView.setItems(mutableListOf(item1, item3, item4), 2) }
        verify { inventoryView.show() }
        confirmVerified(inventoryView)
        assertEquals(103, hero.health)
        assertEquals(104, hero.maximumHealth)
        assertTrue(item4.isUsed)

        inventoryState.handleInput(StateProperties.inventoryItemUp)
        verify { inventoryView.setChosenItem(1) }
        verify { inventoryView.show() }
        inventoryState.handleInput(StateProperties.inventoryItemUp)
        verify { inventoryView.setChosenItem(0) }
        verify { inventoryView.show() }
        inventoryState.handleInput(StateProperties.inventoryItemAction)
        verify { inventoryView.setHeroStats(hero) }
        verify { inventoryView.setItems(mutableListOf(item1, item3, item4), 0) }
        verify { inventoryView.show() }
        confirmVerified(inventoryView)
        assertEquals(102, hero.health)
        assertEquals(104, hero.maximumHealth)
        assertFalse(item1.isUsed)

        inventoryState.handleInput(StateProperties.inventoryItemDown)
        verify { inventoryView.setChosenItem(1) }
        verify { inventoryView.show() }
        inventoryState.handleInput(StateProperties.inventoryItemAction)
        verify { inventoryView.setHeroStats(hero) }
        verify { inventoryView.setItems(mutableListOf(item1, item3, item4), 1) }
        verify { inventoryView.show() }
        confirmVerified(inventoryView)
        assertEquals(103, hero.health)
        assertEquals(104, hero.maximumHealth)
        assertTrue(item3.isUsed)
    }
}
