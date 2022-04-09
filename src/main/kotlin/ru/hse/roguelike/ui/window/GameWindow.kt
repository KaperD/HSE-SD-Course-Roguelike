package ru.hse.roguelike.ui.window

import ru.hse.roguelike.ui.Image

/**
 * Окно в котором отображается игра
 */
interface GameWindow {
    /**
     * Отобразить image в окне
     * @param image То что нужно отобразить
     */
    fun show(image: Image)

    /**
     * Создание Image размерами со всё окно
     * @return созданная Image размерами со всё окно
     */
    fun createImage(): Image
}
