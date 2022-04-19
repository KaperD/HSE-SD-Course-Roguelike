package ru.hse.roguelike.state

import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import ru.hse.roguelike.controller.MapFreeModeController
import ru.hse.roguelike.input.InputType
import ru.hse.roguelike.model.*
import ru.hse.roguelike.model.creature.Hero
import ru.hse.roguelike.property.StateProperties
import ru.hse.roguelike.sound.GameSound
import ru.hse.roguelike.ui.map.MapView
import kotlin.test.assertEquals

internal class MapFreeModeStateTest {
    @Test
    fun `test map free mode`() {
        val hero = Hero(100, 100, 10, Position(0, 0), mutableListOf())
        val field = GameField(
            listOf(
                listOf(Cell(GroundType.Land, mutableListOf(), hero), Cell(GroundType.Water, mutableListOf(), null)),
                listOf(Cell(GroundType.Fire, mutableListOf(), null), Cell(GroundType.LevelEnd, mutableListOf(), null))
            )
        )
        val gameModel = GameModel(field, mutableListOf(), hero)
        val mapView = mockk<MapView>(relaxed = true)
        val gameSound = mockk<GameSound>(relaxed = true)
        val anotherState = mockk<State>()
        val mapFreeModeController = MapFreeModeController(gameModel, mapView, gameSound)
        val mapFreeModeState = MapFreeModeState(mapFreeModeController, gameSound, mapOf(InputType.I to anotherState))

        mapFreeModeState.activate()
        verify { mapView.set(0, 0, field.get(0, 0)) }
        verify { mapView.set(0, 1, field.get(0, 1)) }
        verify { mapView.set(1, 0, field.get(1, 0)) }
        verify { mapView.set(1, 1, field.get(1, 1)) }
        verify { mapView.set(0, 0, field.get(0, 0)) }
        verify { mapView.setHighlighted(0, 0, field.get(0, 0)) }
        verify { mapView.setCellInfo(field.get(0, 0)) }
        verify { mapView.show() }
        confirmVerified(mapView)

        assertEquals(mapFreeModeState, mapFreeModeState.handleInput(StateProperties.freeModeCursorDown))
        verify { mapView.set(0, 0, field.get(0, 0)) }
        verify { mapView.setHighlighted(0, 1, field.get(0, 1)) }
        verify { mapView.setCellInfo(field.get(0, 1)) }
        verify { mapView.show() }
        confirmVerified(mapView)

        assertEquals(mapFreeModeState, mapFreeModeState.handleInput(StateProperties.freeModeCursorRight))
        verify { mapView.set(0, 1, field.get(0, 1)) }
        verify { mapView.setHighlighted(1, 1, field.get(1, 1)) }
        verify { mapView.setCellInfo(field.get(1, 1)) }
        verify { mapView.show() }
        confirmVerified(mapView)

        assertEquals(mapFreeModeState, mapFreeModeState.handleInput(StateProperties.freeModeCursorUp))
        verify { mapView.set(1, 1, field.get(1, 1)) }
        verify { mapView.setHighlighted(1, 0, field.get(1, 0)) }
        verify { mapView.setCellInfo(field.get(1, 0)) }
        verify { mapView.show() }
        confirmVerified(mapView)

        mapFreeModeState.activate()
        verify { mapView.set(0, 0, field.get(0, 0)) }
        verify { mapView.set(0, 1, field.get(0, 1)) }
        verify { mapView.set(1, 0, field.get(1, 0)) }
        verify { mapView.set(1, 1, field.get(1, 1)) }
        verify { mapView.set(0, 0, field.get(0, 0)) }
        verify { mapView.setHighlighted(0, 0, field.get(0, 0)) }
        verify { mapView.setCellInfo(field.get(0, 0)) }
        verify { mapView.show() }
        confirmVerified(mapView)

        assertEquals(anotherState, mapFreeModeState.handleInput(InputType.I))
    }

    @Test
    fun `test map free mode borders`() {
        val hero = Hero(100, 100, 10, Position(0, 0), mutableListOf())
        val field = GameField(
            listOf(
                listOf(Cell(GroundType.Land, mutableListOf(), hero), Cell(GroundType.Water, mutableListOf(), null)),
                listOf(Cell(GroundType.Fire, mutableListOf(), null), Cell(GroundType.LevelEnd, mutableListOf(), null))
            )
        )
        val gameModel = GameModel(field, mutableListOf(), hero)
        val mapView = mockk<MapView>(relaxed = true)
        val gameSound = mockk<GameSound>(relaxed = true)
        val mapFreeModeController = MapFreeModeController(gameModel, mapView, gameSound)
        val mapFreeModeState = MapFreeModeState(mapFreeModeController, gameSound, mapOf())
        mapFreeModeState.activate()

        mapFreeModeState.handleInput(StateProperties.freeModeCursorUp)
        mapFreeModeState.handleInput(StateProperties.freeModeCursorLeft)
        mapFreeModeState.handleInput(StateProperties.freeModeCursorDown)
        mapFreeModeState.handleInput(StateProperties.freeModeCursorDown)
        mapFreeModeState.handleInput(StateProperties.freeModeCursorRight)
        mapFreeModeState.handleInput(StateProperties.freeModeCursorRight)
        mapFreeModeState.handleInput(StateProperties.freeModeCursorUp)
        mapFreeModeState.handleInput(StateProperties.freeModeCursorUp)

        verify(exactly = 5) { gameSound.beep() }
        confirmVerified(gameSound)
    }
}
