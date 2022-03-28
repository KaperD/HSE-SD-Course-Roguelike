package ru.hse.roguelike.state

import ru.hse.roguelike.input.InputType
import ru.hse.roguelike.property.StringProperties
import ru.hse.roguelike.sound.GameSound
import ru.hse.roguelike.ui.help.MessageView

class HelpState(
    private val gameSound: GameSound,
    private val messageView: MessageView
) : State {
    var states: Map<InputType, State> = mapOf()

    init {
        messageView.setText(StringProperties.helpMessage)
    }

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
        messageView.show()
    }
}
