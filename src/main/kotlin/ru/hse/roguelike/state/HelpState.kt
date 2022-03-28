package ru.hse.roguelike.state

import ru.hse.roguelike.GameSound
import ru.hse.roguelike.ui.help.HelpView

class HelpState(
    private val states: Map<InputType, State>,
    private val gameSound: GameSound,
    private val helpView: HelpView
) : State {
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
