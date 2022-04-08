package ru.hse.roguelike.sound

import com.googlecode.lanterna.terminal.Terminal

/**
 * Реализация звука для игры, с помощью библиотеки Lanterna
 * @param terminal терминал, в котором будет издаваться звук
 */
class LanternaGameSound(private val terminal: Terminal) : GameSound {
    /**
     * Издать звук *beep* в соответствующем терминале
     */
    override fun beep() {
        terminal.bell()
    }
}
