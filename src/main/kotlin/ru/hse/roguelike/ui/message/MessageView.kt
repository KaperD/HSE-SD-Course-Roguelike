package ru.hse.roguelike.ui.message

import ru.hse.roguelike.ui.View

/**
 * Отображение сообщение посередине экрана.
 */
interface MessageView : View {
    /**
     * Установить текста сообщения для отображения.
     * @param text Текст для отображения
     */
    fun setText(text: String)
}
