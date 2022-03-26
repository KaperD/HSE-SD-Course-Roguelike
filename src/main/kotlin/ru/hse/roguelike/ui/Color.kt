package ru.hse.roguelike.ui

interface Color {
    val red: Int
    val green: Int
    val blue: Int

    enum class ANSI(
        override val red: Int,
        override val green: Int,
        override val blue: Int
    ) : Color {
        Black(0, 0, 0),
        Red(170, 0, 0),
        Green(0, 170, 0),
        Yellow(170, 85, 0),
        Blue(0, 0, 170),
        Magenta(170, 0, 170),
        Cyan(0, 170, 170),
        White(170, 170, 170),
        Default(0, 0, 0)
    }
}
