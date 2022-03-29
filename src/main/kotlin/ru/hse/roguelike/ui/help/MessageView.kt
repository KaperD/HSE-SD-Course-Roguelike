package ru.hse.roguelike.ui.help

import ru.hse.roguelike.ui.View

interface MessageView : View {
    fun setText(text: String)
}
