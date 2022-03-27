package ru.hse.roguelike.utils

import ru.hse.roguelike.ui.Color
import ru.hse.roguelike.ui.Drawable

fun Drawable.drawText(action: DrawContext.() -> Unit) {
    clear()
    DrawContext(this).action()
}

class DrawContext(private val drawable: Drawable) {
    var y = 0

    fun appendLine(line: String, foreground: Color = Color.ANSI.Black, background: Color = Color.ANSI.Default) {
        drawable.setLine(0, y++, line, foreground, background)
    }

    fun appendText(text: String, foreground: Color = Color.ANSI.Black, background: Color = Color.ANSI.Default) {
        for (line in text.lines()) {
            appendLine(line, foreground, background)
        }
    }

    fun appendTitle(title: String) {
        appendLine(title, foreground = Color.ANSI.TitleColor)
    }
}
