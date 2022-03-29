package ru.hse.roguelike.state

import ru.hse.roguelike.input.InputType
import ru.hse.roguelike.property.StringProperties
import ru.hse.roguelike.ui.help.MessageView

class VictoryState(private val messageView: MessageView) : State {

    init {
        messageView.setText(StringProperties.victory)
    }

    override fun handleInput(type: InputType): State = this

    override fun activate() {
        messageView.show()
    }
}
