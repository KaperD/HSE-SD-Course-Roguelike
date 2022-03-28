package ru.hse.roguelike.state

import ru.hse.roguelike.input.InputType

interface State {
    fun handleInput(type: InputType): State
    fun activate()
}
