package ru.hse.roguelike

import com.googlecode.lanterna.terminal.Terminal

class GameSound(private val terminal: Terminal) {
    fun beep() {
        terminal.bell()
    }
}
