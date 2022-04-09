package ru.hse.roguelike.state

import ru.hse.roguelike.input.InputType
import ru.hse.roguelike.model.GameModel
import ru.hse.roguelike.property.StateProperties
import ru.hse.roguelike.sound.GameSound
import ru.hse.roguelike.ui.map.MapView

/**
 * Состояние исследования карты без передвижения героя.
 * Содержит логику по перемещению курсора и отображении того, что находится в клетке под курсором
 */
class MapFreeModeState(
    private val gameModel: GameModel,
    override val view: MapView,
    override val gameSound: GameSound,
    override val states: Map<InputType, State>
) : State() {
    private var curX = 0
    private var curY = 0

    init {
        actionByInputType[StateProperties.freeModeCursorUp] = { moveCursorTo(curX, curY - 1) }
        actionByInputType[StateProperties.freeModeCursorDown] = { moveCursorTo(curX, curY + 1) }
        actionByInputType[StateProperties.freeModeCursorLeft] = { moveCursorTo(curX - 1, curY) }
        actionByInputType[StateProperties.freeModeCursorRight] = { moveCursorTo(curX + 1, curY) }
    }

    override fun activate() {
        drawWholeField()
        moveCursorTo(gameModel.hero.position.x, gameModel.hero.position.y)
        view.show()
    }

    private fun moveCursorTo(x: Int, y: Int): State {
        if (x in 0 until gameModel.field.width && y in 0 until gameModel.field.height) {
            view.set(curX, curY, gameModel.field.get(curX, curY))
            curX = x
            curY = y
            view.setHighlighted(curX, curY, gameModel.field.get(curX, curY))
            view.setCellInfo(gameModel.field.get(x, y))
        } else {
            gameSound.beep()
        }
        return this
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
