package ru.hse.roguelike.state

import ru.hse.roguelike.input.InputType
import ru.hse.roguelike.property.StringProperties
import ru.hse.roguelike.sound.GameSound
import ru.hse.roguelike.ui.message.MessageView

/**
 * Состояние просмотра информации об игре.
 * Показывает назначение клавиш
 */
class HelpState(
    override val view: MessageView,
    override val gameSound: GameSound,
    override val states: Map<InputType, State>
) : State() {

    init {
        view.setText(StringProperties.helpMessage)
    }

    override fun activate() {
        view.show()
    }
}
