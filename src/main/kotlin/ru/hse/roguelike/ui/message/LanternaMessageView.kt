package ru.hse.roguelike.ui.message

import ru.hse.roguelike.ui.window.GameWindow

/**
 * Реализация отображения сообщения посередине экрана
 * с помощью библиотеки Lanterna
 * @param window Окно для отображения данного view
 * @param text   Исходный текст для отображения
 */
class LanternaMessageView(
    private val window: GameWindow,
    text: String = ""
) : MessageView {
    private val image = window.createImage()

    init {
        setText(text)
    }

    override fun setText(text: String) {
        image.clear()
        if (text.isBlank()) {
            return
        }
        val lines = text.lines()
        val numberOfLines = lines.size
        val maximumLengthOfLine = lines.maxOf { it.length }
        val x = (image.width - maximumLengthOfLine) / 2
        val y = (image.height - numberOfLines) / 2
        image.setText(x, y, text)
    }

    override fun show() {
        window.show(image)
    }
}
