package ru.hse.roguelike.state

import ru.hse.roguelike.input.InputType
import ru.hse.roguelike.sound.GameSound
import ru.hse.roguelike.ui.View

/**
 * Состояние игры.
 * От состояния зависит, что будет изображено на экране и
 * что будет происходить при нажатии определенной кнопки пользователем.
 */
abstract class State {
    /**
     * Сущность для вывода на экран
     */
    protected abstract val view: View

    /**
     * Сущность для создания звуков
     */
    protected abstract val gameSound: GameSound

    /**
     * Переходы в следующие состояния
     */
    protected abstract val states: Map<InputType, State>

    /**
     * Реакции на соответсвующий ввод пользователя
     */
    protected val actionByInputType = mutableMapOf<InputType, () -> State>()

    /**
     * Обработка очередного ввода пользователя.
     * @param type тип ввода
     * @return следующее состояние
     */
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

    /**
     * Активация состояния
     */
    abstract fun activate()
}
