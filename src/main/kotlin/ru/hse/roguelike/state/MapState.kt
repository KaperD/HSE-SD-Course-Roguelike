package ru.hse.roguelike.state

import ru.hse.roguelike.controller.MapController
import ru.hse.roguelike.input.InputType
import ru.hse.roguelike.property.StateProperties
import ru.hse.roguelike.sound.GameSound

/**
 * Состояние передвижения героя по карте
 */
class MapState(
    private val mapController: MapController,
    private val gameOverState: State,
    private val victoryState: State,
    override val gameSound: GameSound,
    override val states: Map<InputType, State>
) : State() {

    init {
        actionByInputType[StateProperties.moveUp] = {
            mapController.moveHeroUp()
            nextState()
        }
        actionByInputType[StateProperties.moveDown] = {
            mapController.moveHeroDown()
            nextState()
        }
        actionByInputType[StateProperties.moveLeft] = {
            mapController.moveHeroLeft()
            nextState()
        }
        actionByInputType[StateProperties.moveRight] = {
            mapController.moveHeroRight()
            nextState()
        }
    }

    override fun activate() {
        mapController.showMap()
    }

    private fun nextState(): State {
        return when {
            mapController.isVictory() -> victoryState
            mapController.isGameOver() -> gameOverState
            else -> this
        }
    }
}
