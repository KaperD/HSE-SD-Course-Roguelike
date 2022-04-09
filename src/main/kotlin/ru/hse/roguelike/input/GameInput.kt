package ru.hse.roguelike.input

/**
 * Ввод игрока для игры
 */
interface GameInput {
    /**
     * Получение ввода от игрока
     * @return то что ввёл игрок
     */
    fun getInput(): InputType
}
