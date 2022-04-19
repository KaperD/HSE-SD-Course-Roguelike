package ru.hse.roguelike.state

import ru.hse.roguelike.input.InputType
import ru.hse.roguelike.property.StringProperties
import ru.hse.roguelike.sound.GameSound
import ru.hse.roguelike.ui.message.MessageView

/**
 * Состояние проигранной игры. Показывает сообщение о том, что игрок проиграл.
 * Из данного состояния нельзя перейти ни в какие другие
 */
class GameOverState(
    val view: MessageView,
    override val gameSound: GameSound
) : State() {
    override val states: Map<InputType, State> = emptyMap()

    init {
        view.setText(StringProperties.gameOver)
    }

    override fun handleInput(type: InputType): State = this.also { gameSound.beep() }

    override fun activate() {
        view.show()
    }
}
