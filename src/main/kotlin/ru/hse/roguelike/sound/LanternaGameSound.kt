package ru.hse.roguelike.sound

import com.googlecode.lanterna.terminal.Terminal

class LanternaGameSound(private val terminal: Terminal) : GameSound {

    override fun beep() {
        terminal.bell()
    }
}
