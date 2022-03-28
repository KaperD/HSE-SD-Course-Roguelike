package ru.hse.roguelike.input

import com.googlecode.lanterna.terminal.Terminal

class LanternaGameInput(private val terminal: Terminal) : GameInput {

    override fun getInput(): InputType = terminal.readInput().toInputType()
}
