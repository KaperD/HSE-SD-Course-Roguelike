package ru.hse.roguelike.controller

import ru.hse.roguelike.model.GameModel
import ru.hse.roguelike.sound.GameSound
import ru.hse.roguelike.ui.map.MapView

/**
 * Содержит логику по перемещению курсора и отображении того, что находится в клетке под курсором
 */
class MapFreeModeController(
    private val gameModel: GameModel,
    private val view: MapView,
    private val gameSound: GameSound
) {
    private var curX = 0
    private var curY = 0

    /**
     * Подвинуть курсор вверх
     */
    fun moveCursorUp() {
        moveCursorTo(curX, curY - 1)
    }

    /**
     * Подвинуть курсор вниз
     */
    fun moveCursorDown() {
        moveCursorTo(curX, curY + 1)
    }

    /**
     * Подвинуть курсор влево
     */
    fun moveCursorLeft() {
        moveCursorTo(curX - 1, curY)
    }

    /**
     * Подвинуть курсор вправо
     */
    fun moveCursorRight() {
        moveCursorTo(curX + 1, curY)
    }

    /**
     * Перерисовать всё представление и поставить курсор на героя
     */
    fun showMapFreeMode() {
        drawWholeField()
        moveCursorTo(gameModel.hero.position.x, gameModel.hero.position.y)
        view.show()
    }

    private fun moveCursorTo(x: Int, y: Int) {
        if (x in 0 until gameModel.field.width && y in 0 until gameModel.field.height) {
            view.set(curX, curY, gameModel.field.get(curX, curY))
            curX = x
            curY = y
            view.setHighlighted(curX, curY, gameModel.field.get(curX, curY))
            view.setCellInfo(gameModel.field.get(x, y))
            view.show()
        } else {
            gameSound.beep()
        }
    }

    private fun drawWholeField() {
        val field = gameModel.field
        for (y in 0 until field.height) {
            for (x in 0 until field.width) {
                view.set(x, y, field.get(x, y))
            }
        }
    }
}
