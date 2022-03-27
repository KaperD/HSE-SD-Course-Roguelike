package ru.hse.roguelike.state

interface State {
    fun handleInput(type: InputType): State
    fun activate()
}
