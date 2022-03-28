package ru.hse.roguelike.ui.help

import ru.hse.roguelike.property.StringProperties.helpMessage
import ru.hse.roguelike.ui.window.GameWindow

class LanternaHelpView(
    private val window: GameWindow,
    text: String = helpMessage
) : HelpView {
    private val image = window.createImage()

    init {
        setText(text)
    }

    override fun setText(text: String) {
        image.clear()
        image.setText(0, 0, text)
    }

    override fun show() {
        window.show(image)
    }
}
