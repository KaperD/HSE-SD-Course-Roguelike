package ru.hse.roguelike.ui.message

import ru.hse.roguelike.ui.View

interface MessageView : View {
    fun setText(text: String)
}
