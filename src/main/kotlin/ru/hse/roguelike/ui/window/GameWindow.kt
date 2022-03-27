package ru.hse.roguelike.ui.window

import ru.hse.roguelike.ui.Image

interface GameWindow {
    fun show(image: Image)
    fun createImage(): Image
}
