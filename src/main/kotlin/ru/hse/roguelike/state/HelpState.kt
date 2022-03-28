package ru.hse.roguelike.state

import ru.hse.roguelike.input.InputType
import ru.hse.roguelike.sound.GameSound
import ru.hse.roguelike.ui.help.HelpView

class HelpState(
    private val gameSound: GameSound,
    private val helpView: HelpView
) : State {
    var states: Map<InputType, State> = mapOf()

    override fun handleInput(type: InputType): State {
        val newState = states[type]
        return if (newState != null) {
            newState
        } else {
            gameSound.beep()
            this
        }
    }

    override fun activate() {
        helpView.show()
    }
}
