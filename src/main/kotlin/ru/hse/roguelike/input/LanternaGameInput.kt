package ru.hse.roguelike.input

import com.googlecode.lanterna.terminal.Terminal

/**
 * Ввод игрока для игры.
 * Реализованный с помощью библиотеки Lanterna.
 * @param terminal терминал, в котором запускается игра
 */
class LanternaGameInput(private val terminal: Terminal) : GameInput {
    /**
     * Получение ввода от игрока в блокирующем режиме.
     * @return ввод игрока
     */
    override fun getInput(): InputType = terminal.readInput().toInputType()
}
