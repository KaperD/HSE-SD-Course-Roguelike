package ru.hse.roguelike.state

enum class InputType {
    m,
    f,
    i,
    h,
    ArrowUp,
    ArrowDown,
    Enter;

    companion object {
        fun String.toInputType() = valueOf(this)
    }
}
