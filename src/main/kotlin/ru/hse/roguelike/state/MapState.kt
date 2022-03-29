package ru.hse.roguelike.state

import ru.hse.roguelike.factory.GameFieldFactory
import ru.hse.roguelike.input.InputType
import ru.hse.roguelike.model.GameModel
import ru.hse.roguelike.model.GroundType
import ru.hse.roguelike.model.Position
import ru.hse.roguelike.property.GameProperties
import ru.hse.roguelike.property.StateProperties
import ru.hse.roguelike.sound.GameSound
import ru.hse.roguelike.ui.map.MapView

class MapState(
    private val gameModel: GameModel,
    override val view: MapView,
    override val gameSound: GameSound,
    override val states: Map<InputType, State>,
    private val gameFieldFactory: GameFieldFactory,
    private val gameProperties: GameProperties,
    private val gameOverState: State,
    private val victoryState: State,
    levelOrder: List<String>
) : State() {
    private val levelIterator = levelOrder.iterator()
    private val hero = gameModel.hero

    init {
        moveToNextLevel()

        actionByInputType[StateProperties.moveUp] = { tryMoveHeroTo(hero.position.x, hero.position.y - 1) }
        actionByInputType[StateProperties.moveDown] = { tryMoveHeroTo(hero.position.x, hero.position.y + 1) }
        actionByInputType[StateProperties.moveLeft] = { tryMoveHeroTo(hero.position.x - 1, hero.position.y) }
        actionByInputType[StateProperties.moveRight] = { tryMoveHeroTo(hero.position.x + 1, hero.position.y) }
    }

    override fun activate() {
        drawWholeField()
        drawHeroStats()
        view.show()
    }

    private fun tryMoveHeroTo(x: Int, y: Int): State {
        return if (canMoveHeroTo(x, y)) {
            moveHeroTo(x, y)
            val newCell = gameModel.field.get(x, y)
            if (newCell.items.isNotEmpty()) {
                hero.items.addAll(newCell.items)
                newCell.items.clear()
            }
            when (newCell.groundType) {
                GroundType.Fire -> hero.health -= gameProperties.fireDamage
                GroundType.LevelEnd -> {
                    if (levelIterator.hasNext()) {
                        moveToNextLevel()
                        drawWholeField()
                    } else {
                        return victoryState
                    }
                }
                else -> {}
            }
            drawHeroStats()
            if (heroIsDead()) {
                gameOverState
            } else {
                this
            }
        } else {
            gameSound.beep()
            this
        }
    }

    private fun heroIsDead(): Boolean {
        return hero.health <= 0
    }

    private fun moveToNextLevel() {
        val levelName = levelIterator.next()
        val (field, heroPosition) = gameFieldFactory.getByLevelName(levelName)
        gameModel.hero.position = heroPosition
        field.get(heroPosition).creature = gameModel.hero
        gameModel.field = field
    }

    private fun moveHeroTo(x: Int, y: Int) {
        val (oldHeroX, oldHeroY) = hero.position
        gameModel.field.get(hero.position).creature = null
        hero.position = Position(x, y)
        gameModel.field.get(hero.position).creature = hero
        drawCell(oldHeroX, oldHeroY)
        drawCell(x, y)
    }

    private fun canMoveHeroTo(x: Int, y: Int): Boolean {
        return x in 0 until gameModel.field.width &&
            y in 0 until gameModel.field.height &&
            gameModel.field.get(x, y).groundType in passableGroundTypes
    }

    private fun drawCell(x: Int, y: Int) {
        view.set(x, y, gameModel.field.get(x, y))
    }

    private fun drawHeroStats() {
        view.setHeroStats(gameModel.hero)
    }

    private fun drawWholeField() {
        val field = gameModel.field
        for (y in 0 until field.height) {
            for (x in 0 until field.width) {
                view.set(x, y, field.get(x, y))
            }
        }
    }

    companion object {
        private val passableGroundTypes = setOf(GroundType.LevelEnd, GroundType.Land, GroundType.Fire)
    }
}
