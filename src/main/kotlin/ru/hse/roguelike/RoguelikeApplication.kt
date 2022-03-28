package ru.hse.roguelike

import ru.hse.roguelike.input.GameInput
import ru.hse.roguelike.property.StateProperties
import ru.hse.roguelike.state.State

class RoguelikeApplication(
    private val gameInput: GameInput,
    private var previousState: State
) {

    fun run() {
        previousState.activate()
        while (true) {
            val inputType = gameInput.getInput()
            if (inputType == StateProperties.exitGame) {
                return
            }
            val newState = previousState.handleInput(inputType)
            if (newState != previousState) {
                newState.activate()
            }
            previousState = newState
        }
    }
}
