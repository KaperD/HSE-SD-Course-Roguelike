package ru.hse.roguelike.input

import com.googlecode.lanterna.input.KeyStroke
import com.googlecode.lanterna.input.KeyType

enum class InputType {
    M,
    F,
    I,
    H,
    ArrowUp,
    ArrowDown,
    Enter,
    Esc,
    Unknown;
}

fun String.toInputType() = InputType.valueOf(this)

fun KeyStroke.toInputType(): InputType = when (this.keyType) {
    KeyType.ArrowUp -> InputType.ArrowUp
    KeyType.ArrowDown -> InputType.ArrowDown
    KeyType.Enter -> InputType.Enter
    KeyType.Escape -> InputType.Esc
    KeyType.Character -> when (this.character) {
        'm' -> InputType.M
        'f' -> InputType.F
        'i' -> InputType.I
        'h' -> InputType.H
        else -> InputType.Unknown
    }
    else -> InputType.Unknown
}
