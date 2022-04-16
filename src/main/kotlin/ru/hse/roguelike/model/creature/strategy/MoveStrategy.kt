package ru.hse.roguelike.model.creature.strategy

import ru.hse.roguelike.model.GameField
import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.Hero
import ru.hse.roguelike.model.creature.mob.Mob
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import kotlin.math.sign

/**
 * Стратегия передвижения моба
 */
interface MoveStrategy {
    /**
     * @param gameField игровое поле, на котором находится моб
     * @param mob моб, которого нужно передвинуть
     *
     * @return позицию, в которую нужно переместить моба
     */
    fun move(gameField: GameField, mob: Mob): Position

    fun canSeeHero(gameField: GameField, heroPosition: Position, mobPosition: Position): Boolean {
        return if (heroPosition.x == mobPosition.x) {
            canSeeHeroOnSameColumn(gameField, mobPosition, heroPosition)
        } else {
            canSeeHeroOnOtherColumn(gameField, mobPosition, heroPosition)
        }
    }

    private fun canSeeHeroOnSameColumn(gameField: GameField, mobPosition: Position, heroPosition: Position): Boolean {
        val heroRelativePosition = Position(heroPosition.x - mobPosition.x, heroPosition.y - mobPosition.y)
        val directionY = heroRelativePosition.y.sign
        for (y in 1 until heroRelativePosition.y.absoluteValue) {
            val potentialBlockPosition = Position(
                mobPosition.x,
                mobPosition.y + directionY * y
            )
            if (!gameField.get(potentialBlockPosition).groundType.canSeeThrough) {
                return false
            }
        }
        return true
    }

    private fun canSeeHeroOnOtherColumn(gameField: GameField, mobPosition: Position, heroPosition: Position): Boolean {
        val heroRelativePosition = Position(heroPosition.x - mobPosition.x, heroPosition.y - mobPosition.y)
        fun line(x: Int): Float = heroRelativePosition.y.toFloat() / heroRelativePosition.x * x
        val directionX = heroRelativePosition.x.sign
        val floatBase = 10.0
        for (x in 1 until heroRelativePosition.x.absoluteValue) {
            val y = (line(directionX * x) * floatBase).roundToInt()
            val potentialBlockPosition =
                Position(mobPosition.x + directionX * x, mobPosition.y + (y / floatBase).roundToInt())
            if (checkGameFieldBounds(gameField, potentialBlockPosition) &&
                !gameField.get(potentialBlockPosition).groundType.canSeeThrough
            ) {
                return false
            }
        }
        return true
    }

    fun getAvailableNextPositions(gameField: GameField, position: Position): List<Position> {
        val potentialPositions = listOf(
            position.copy(x = position.x + 1),
            position.copy(x = position.x - 1),
            position.copy(y = position.y + 1),
            position.copy(y = position.y - 1)
        )
        return potentialPositions.filter { isPositionAvailable(gameField, it) }
    }

    private fun isPositionAvailable(gameField: GameField, position: Position): Boolean {
        return if (!checkGameFieldBounds(gameField, position)) {
            false
        } else {
            val cell = gameField.get(position)
            cell.groundType.isPassable && (cell.creature == null || cell.creature is Hero)
        }
    }

    private fun checkGameFieldBounds(gameField: GameField, position: Position): Boolean {
        return 0 <= position.x && position.x < gameField.width && 0 <= position.y && position.y < gameField.height
    }
}
