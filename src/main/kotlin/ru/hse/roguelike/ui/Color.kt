package ru.hse.roguelike.ui

import com.googlecode.lanterna.TextColor

/**
 * Определение цветов для отображения игры
 */
interface Color {
    val red: Int
    val green: Int
    val blue: Int
    val textColor: TextColor

    enum class ANSI(
        override val textColor: TextColor
    ) : Color {
        Black(TextColor.ANSI.BLACK),
        Red(TextColor.ANSI.RED),
        Green(TextColor.ANSI.GREEN),
        Yellow(TextColor.ANSI.YELLOW),
        Blue(TextColor.ANSI.BLUE),
        Magenta(TextColor.ANSI.MAGENTA),
        Cyan(TextColor.ANSI.CYAN),
        White(TextColor.ANSI.WHITE),
        BlackBright(TextColor.ANSI.BLACK_BRIGHT),
        RedBright(TextColor.ANSI.RED_BRIGHT),
        GreenBright(TextColor.ANSI.GREEN_BRIGHT),
        YellowBright(TextColor.ANSI.YELLOW_BRIGHT),
        BlueBright(TextColor.ANSI.BLUE_BRIGHT),
        MagentaBright(TextColor.ANSI.MAGENTA_BRIGHT),
        CyanBright(TextColor.ANSI.CYAN_BRIGHT),
        WhiteBright(TextColor.ANSI.WHITE_BRIGHT);

        override val red: Int = textColor.red
        override val green: Int = textColor.green
        override val blue: Int = textColor.blue
    }
}

/**
 * Получение цвета по строковому представлению
 */
fun String.toColor(): Color = Color.ANSI.valueOf(this)
