package ru.hse.roguelike.controller

import ru.hse.roguelike.factory.item.ItemFactory
import ru.hse.roguelike.factory.mob.FantasyMobFactory
import ru.hse.roguelike.factory.mob.SwampMobFactory
import ru.hse.roguelike.model.GameField
import ru.hse.roguelike.model.GameModel
import ru.hse.roguelike.model.GroundType
import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.mob.Mob
import ru.hse.roguelike.model.creature.mob.decorator.RandomMobDecorator
import ru.hse.roguelike.property.GameProperties
import ru.hse.roguelike.sound.GameSound
import ru.hse.roguelike.ui.map.MapView

/**
 * Содержит логику по проверке и передвижении героя, подборе предметов, атаке врагов.
 * Также показывает игроку текущие характеристики героя
 */
class MapController(
    private val gameModel: GameModel,
    private val view: MapView,
    private val gameSound: GameSound,
    private val itemFactory: ItemFactory,
    private val gameProperties: GameProperties
) {
    private val levelIterator = gameProperties.levelsOrder.iterator()
    private val hero = gameModel.hero
    private var isGameOver = false
    private var isVictory = false

    init {
        moveToNextLevel()
    }

    /**
     * Передвинуть героя вверх
     */
    fun moveHeroUp() {
        tryMoveHeroTo(hero.position.x, hero.position.y - 1)
    }

    /**
     * Передвинуть героя вниз
     */
    fun moveHeroDown() {
        tryMoveHeroTo(hero.position.x, hero.position.y + 1)
    }

    /**
     * Передвинуть героя влево
     */
    fun moveHeroLeft() {
        tryMoveHeroTo(hero.position.x - 1, hero.position.y)
    }

    /**
     * Передвинуть героя вправо
     */
    fun moveHeroRight() {
        tryMoveHeroTo(hero.position.x + 1, hero.position.y)
    }

    /**
     * Перерисовать всё представление
     */
    fun showMap() {
        drawWholeField()
        drawHeroStats()
        view.show()
    }

    /**
     * Выиграл ли игрок
     */
    fun isGameOver(): Boolean = isGameOver

    /**
     * Проиграл ли игрок
     */
    fun isVictory(): Boolean = isVictory

    private fun tryMoveHeroTo(x: Int, y: Int) {
        require(!isVictory && !isGameOver) { "Can't move hero when game is over" }
        if (canMoveHeroTo(x, y)) {
            val newCell = gameModel.field.get(x, y)
            val newCellCreature = newCell.creature
            if (newCellCreature is Mob) {
                newCellCreature.health -= hero.attackDamage
                hero.health -= newCellCreature.attackDamage
                if (newCellCreature.health <= 0) {
                    increaseHeroExperienceBy(gameProperties.mobKillExperience)
                    newCell.creature = null
                    gameModel.mobs = gameModel.mobs.filter { it != newCellCreature }
                }
                if (gameProperties.confusionTime > 0) {
                    val confusedCreature = RandomMobDecorator(newCellCreature, gameProperties.confusionTime)
                    gameModel.mobs = gameModel.mobs.map { if (it == newCellCreature) confusedCreature else it }
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
                            isVictory = true
                            return
                        }
                    }
                    else -> {}
                }
            }
            gameModel.mobs = gameModel.mobs.flatMap {
                val newMobsStates = it.move(gameModel.field)
                newMobsStates.mapNotNull { newMobState ->
                    if (newMobState.health <= 0) {
                        increaseHeroExperienceBy(gameProperties.mobKillExperience)
                        gameModel.field.get(newMobState.position).creature = null
                        null
                    } else {
                        gameModel.field.get(newMobState.position).creature = newMobState
                        newMobState
                    }
                }
            }
            drawWholeField()
            drawHeroStats()
            if (heroIsDead()) {
                isGameOver = true
                return
            }
            view.show()
        } else {
            gameSound.beep()
        }
    }

    private fun increaseHeroExperienceBy(amount: Int) {
        hero.experienceForNextLevel -= amount
        if (hero.experienceForNextLevel <= 0) {
            val extraExperience = -hero.experienceForNextLevel
            val levelChange = 1 + extraExperience / gameProperties.newLevelExperienceAmount
            val restExtraExperience = extraExperience % gameProperties.newLevelExperienceAmount
            hero.experienceForNextLevel = gameProperties.newLevelExperienceAmount - restExtraExperience
            hero.level += levelChange
            hero.health += gameProperties.newLevelHealthChange * levelChange
            hero.maximumHealth += gameProperties.newLevelMaximumHealthChange * levelChange
            hero.attackDamage += gameProperties.newLevelAttackDamageChange * levelChange
        }
    }

    private fun heroIsDead(): Boolean {
        return hero.health <= 0
    }

    private fun moveToNextLevel() {
        val (levelName, mobsType) = levelIterator.next()
        val mobsFactory = when (mobsType) {
            "fantasy" -> FantasyMobFactory()
            else -> SwampMobFactory()
        }
        val (field, mobs, heroPosition) = if (levelName == "?") {
            GameField.builder()
                .generateRandom()
                .withWidth(gameProperties.mapWidth)
                .withHeight(gameProperties.mapHeight)
                .withItemFactory(itemFactory)
                .withMobFactory(mobsFactory)
                .withNumberOfMobs(gameProperties.numberOfMobsOnRandomMap)
                .withNumberOfItems(gameProperties.numberOfItemsOnRandomMap)
                .build()
        } else {
            GameField.builder()
                .loadFromFile(levelName)
                .withWidth(gameProperties.mapWidth)
                .withHeight(gameProperties.mapHeight)
                .withItemFactory(itemFactory)
                .withMobFactory(mobsFactory)
                .build()
        }
        gameModel.hero.position = heroPosition
        gameModel.mobs = mobs
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
