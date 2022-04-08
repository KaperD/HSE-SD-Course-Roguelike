package ru.hse.roguelike.ui

import ru.hse.roguelike.property.ColorProperties.defaultColor
import ru.hse.roguelike.property.ColorProperties.textColor

/**
 * То что можно отобразить пользователю.
 * Обязательно прямоугольное
 */
interface Drawable {
    /**
     * Ширина отображаемого (измеряется в ячейках)
     */
    val width: Int

    /**
     * Высота отображаемого (измеряется в ячейках)
     */
    val height: Int

    /**
     * Установка ячейки в определённой позиции данного drawable
     * @param x          Позиция по горизонтали
     * @param y          Позиция по вертикали
     * @param symbol     Символ
     * @param foreground Цвет фона символа
     * @param background Цвет символа
     */
    fun set(x: Int, y: Int, symbol: Char, foreground: Color = textColor, background: Color = defaultColor)

    /**
     * Установка в данном drawable строки начиная с определённой позиции.
     * Если строка не влезает по вертикали, то делаются переносы
     * @param x          Позиция начала по горизонтали
     * @param y          Позиция начала по вертикали
     * @param line       Строка для установки
     * @param foreground Цвет строки
     * @param background Фон строки
     * @return Количество строк, которые заняла переданная строка при установке
     */
    fun setLine(x: Int, y: Int, line: String, foreground: Color = textColor, background: Color = defaultColor): Int

    /**
     * Установка в данном drawable текста (несколько строк) начиная с определённой позиции.
     * Если строки текста не влезают по вертикали, делаются переносы
     * @param x          Позиция начала по горизонтали
     * @param y          Позиция начала по вертикали
     * @param text       Текст для установки
     * @param foreground Цвет текста
     * @param background Фон текста
     * @return number of image lines taken to draw given line
     */
    fun setText(x: Int, y: Int, text: String, foreground: Color = textColor, background: Color = defaultColor): Int

    /**
     * Получение drawable являющего какой-то часть данного drawable
     * @param topLeftX Позиция по горизонтали верхнего левого угла выделяемой части
     * @param topLeftY Позиция по вертикали верхнего левого угла выделяемой части
     * @param width    Ширина выделямого
     * @param height   Высота выделяемого
     */
    fun subImage(topLeftX: Int, topLeftY: Int, width: Int, height: Int): Drawable

    /**
     * Наполнение всего drawable определённым символом
     * @param symbol     Символ
     * @param foreground Цвет символа
     * @param background Фон символа
     */
    fun fill(symbol: Char, foreground: Color = textColor, background: Color = defaultColor)

    /**
     * Очищение всего drawable
     */
    fun clear()
}
