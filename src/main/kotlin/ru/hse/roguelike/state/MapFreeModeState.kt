package ru.hse.roguelike.state

import ru.hse.roguelike.input.InputType
import ru.hse.roguelike.model.GameModel
import ru.hse.roguelike.property.StateProperties
import ru.hse.roguelike.sound.GameSound
import ru.hse.roguelike.ui.map.MapView

class MapFreeModeState(
    private val gameModel: GameModel,
    override val view: MapView,
    override val gameSound: GameSound,
    override val states: Map<InputType, State>
) : State() {
    private var curX = 0
    private var curY = 0

    init {
        actionByInputType[StateProperties.freeModeCursorUp] = this::moveCursorUp
        actionByInputType[StateProperties.freeModeCursorDown] = this::moveCursorDown
        actionByInputType[StateProperties.freeModeCursorLeft] = this::moveCursorLeft
        actionByInputType[StateProperties.freeModeCursorRight] = this::moveCursorRight
    }

    override fun activate() {
        drawWholeField()
        highlight(gameModel.hero.position.x, gameModel.hero.position.y)
        view.show()
    }

    private fun moveCursorUp() {
        if (curY > 0) {
            highlight(curX, curY - 1)
        } else {
            gameSound.beep()
        }
    }

    private fun moveCursorDown() {
        if (curY + 1 < gameModel.field.height) {
            highlight(curX, curY + 1)
        } else {
            gameSound.beep()
        }
    }

    private fun moveCursorLeft() {
        if (curX > 0) {
            highlight(curX - 1, curY)
        } else {
            gameSound.beep()
        }
    }

    private fun moveCursorRight() {
        if (curX + 1 < gameModel.field.width) {
            highlight(curX + 1, curY)
        } else {
            gameSound.beep()
        }
    }

    private fun highlight(x: Int, y: Int) {
        view.set(curX, curY, gameModel.field.get(curX, curY))
        curX = x
        curY = y
        view.setHighlighted(curX, curY, gameModel.field.get(curX, curY))
        view.setCellInfo(gameModel.field.get(x, y))
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
