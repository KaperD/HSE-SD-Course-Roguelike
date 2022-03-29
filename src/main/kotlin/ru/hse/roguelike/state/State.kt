package ru.hse.roguelike.state

import ru.hse.roguelike.input.InputType
import ru.hse.roguelike.sound.GameSound
import ru.hse.roguelike.ui.View

abstract class State {
    protected abstract val view: View
    protected abstract val gameSound: GameSound
    protected abstract val states: Map<InputType, State>
    protected val actionByInputType = mutableMapOf<InputType, () -> State>()

    open fun handleInput(type: InputType): State {
        val action = actionByInputType[type]
        val newState = states[type]
        return if (action != null) {
            action().also {
                view.show()
            }
        } else if (newState != null) {
            newState
        } else {
            gameSound.beep()
            this
        }
    }
    abstract fun activate()
}
