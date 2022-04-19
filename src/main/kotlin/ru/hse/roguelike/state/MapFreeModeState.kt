package ru.hse.roguelike.state

import ru.hse.roguelike.controller.MapFreeModeController
import ru.hse.roguelike.input.InputType
import ru.hse.roguelike.property.StateProperties
import ru.hse.roguelike.sound.GameSound

/**
 * Состояние исследования карты без передвижения героя
 */
class MapFreeModeState(
    private val mapFreeModeController: MapFreeModeController,
    override val gameSound: GameSound,
    override val states: Map<InputType, State>
) : State() {

    init {
        actionByInputType[StateProperties.freeModeCursorUp] = {
            mapFreeModeController.moveCursorUp()
            this
        }
        actionByInputType[StateProperties.freeModeCursorDown] = {
            mapFreeModeController.moveCursorDown()
            this
        }
        actionByInputType[StateProperties.freeModeCursorLeft] = {
            mapFreeModeController.moveCursorLeft()
            this
        }
        actionByInputType[StateProperties.freeModeCursorRight] = {
            mapFreeModeController.moveCursorRight()
            this
        }
    }

    override fun activate() {
        mapFreeModeController.showMapFreeMode()
    }
}
