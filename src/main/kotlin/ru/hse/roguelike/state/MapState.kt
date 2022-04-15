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

/**
 * Состояние передвижения героя по карте.
 * Содержит логику по проверке и передвижении героя, подборе предметов.
 * Также показывает игроку текущие характеристики героя
 */
class MapState(
    private val gameModel: GameModel,
    override val view: MapView,
    override val gameSound: GameSound,
    override val states: Map<InputType, State>,
    private val gameFieldFactory: GameFieldFactory,
    private val gameProperties: GameProperties,
    private val gameOverState: State,
    private val victoryState: State,
) : State() {
    private val levelIterator = gameProperties.levelsOrder.iterator()
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
            val newCell = gameModel.field.get(x, y)
            val newCellCreature = newCell.creature
            if (newCellCreature != null) {
                newCellCreature.health -= hero.attackDamage
                hero.health -= newCellCreature.attackDamage
                if (newCellCreature.health <= 0) {
                    increaseHeroExperienceBy(gameProperties.mobKillExperience)
                    newCell.creature = null
                    gameModel.mobs = gameModel.mobs.filter { it != newCellCreature }
                }
            } else {
                moveHeroTo(x, y)
                if (newCell.items.isNotEmpty()) {
                    hero.items.addAll(newCell.items)
                    newCell.items.clear()
                }
                when (newCell.groundType) {
                    GroundType.Fire -> hero.health -= gameProperties.fireDamage
                    GroundType.LevelEnd -> {
                        if (levelIterator.hasNext()) {
                            moveToNextLevel()
                        } else {
                            return victoryState
                        }
                    }
                    else -> {}
                }
            }
            gameModel.mobs = gameModel.mobs.mapNotNull {
                val newMobState = it.move(gameModel.field)
                if (newMobState.health <= 0) {
                    increaseHeroExperienceBy(gameProperties.mobKillExperience)
                    gameModel.field.get(newMobState.position).creature = null
                    null
                } else {
                    newMobState
                }
            }
            drawWholeField()
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

    private fun increaseHeroExperienceBy(amount: Int) {
        val oldLevel = hero.experience / gameProperties.newLevelExperienceAmount
        hero.experience += amount
        val newLevel = hero.experience / gameProperties.newLevelExperienceAmount
        val levelsChange = newLevel - oldLevel
        hero.health += gameProperties.newLevelHealthChange * levelsChange
        hero.maximumHealth += gameProperties.newLevelMaximumHealthChange * levelsChange
        hero.attackDamage += gameProperties.newLevelAttackDamageChange * levelsChange
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
        gameModel.field.get(hero.position).creature = null
        hero.position = Position(x, y)
        gameModel.field.get(hero.position).creature = hero
    }

    private fun canMoveHeroTo(x: Int, y: Int): Boolean {
        return x in 0 until gameModel.field.width &&
            y in 0 until gameModel.field.height &&
            gameModel.field.get(x, y).groundType.isPassable
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
}
