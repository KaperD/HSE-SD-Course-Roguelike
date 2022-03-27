package ru.hse.roguelike.state

import ru.hse.roguelike.GameSound
import ru.hse.roguelike.property.StateProperties.Companion.openMap
import ru.hse.roguelike.ui.help.HelpView

class HelpState(
    private val states: Map<InputType, State>,
    private val gameSound: GameSound,
    private val helpView: HelpView
) : State {
    override fun handleInput(type: InputType): State {
        return when (type) {
            openMap -> states[openMap]!!
            else -> {
                gameSound.beep()
                this
            }
        }
    }

    override fun activate() {
        helpView.show()
    }
}
