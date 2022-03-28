package ru.hse.roguelike.state

enum class InputType {
    m,
    f,
    i,
    h,
    ArrowUp,
    ArrowDown,
    Enter;
}

fun String.toInputType() = InputType.valueOf(this)
