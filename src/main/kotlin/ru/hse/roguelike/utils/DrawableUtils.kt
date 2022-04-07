package ru.hse.roguelike.utils

import ru.hse.roguelike.property.ColorProperties.defaultColor
import ru.hse.roguelike.property.ColorProperties.textColor
import ru.hse.roguelike.property.ColorProperties.titleColor
import ru.hse.roguelike.ui.Color
import ru.hse.roguelike.ui.Drawable

fun Drawable.drawText(action: DrawContext.() -> Unit) {
    clear()
    DrawContext(this).action()
}

class DrawContext(private val drawable: Drawable) {
    var curY = 0

    fun appendLine(line: String, foreground: Color = textColor, background: Color = defaultColor) {
        curY += drawable.setLine(0, curY, line, foreground, background)
    }

    fun appendText(text: String, foreground: Color = textColor, background: Color = defaultColor) {
        curY += drawable.setText(0, curY, text, foreground, background)
    }

    fun appendTitle(title: String) {
        appendLine(title, foreground = titleColor)
    }
}
