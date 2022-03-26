package ru.hse.roguelike.ui

interface GameWindow {
    fun show(image: Image)
    fun createImage(): Image
}
