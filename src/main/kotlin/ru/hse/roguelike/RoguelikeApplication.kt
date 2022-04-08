package ru.hse.roguelike

import ru.hse.roguelike.input.GameInput
import ru.hse.roguelike.property.StateProperties
import ru.hse.roguelike.state.State

/**
 * Основной класс приложения, содержащий основной цикл игры
 * @param gameInput     Источник ввода для игры
 * @param previousState Исходное состояние для игры
 */
class RoguelikeApplication(
    private val gameInput: GameInput,
    private var previousState: State
) {

    /**
     * Начни основной цикл игры
     */
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
