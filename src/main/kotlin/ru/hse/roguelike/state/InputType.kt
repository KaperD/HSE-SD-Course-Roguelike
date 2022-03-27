package ru.hse.roguelike.state

enum class InputType {
    m,
    f,
    i,
    h;

    companion object {
        fun String.toInputType() = valueOf(this)
    }
}
