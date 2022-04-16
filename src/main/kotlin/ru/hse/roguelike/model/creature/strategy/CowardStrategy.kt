package ru.hse.roguelike.model.creature.strategy

import ru.hse.roguelike.model.GameField
import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.Hero
import ru.hse.roguelike.model.creature.Mob
import kotlin.math.absoluteValue

class CowardStrategy(val vision: Int) : MoveStrategy {
    override fun move(gameField: GameField, mob: Mob): Position {
        val heroPosition = findHero(gameField, mob.position) ?: return mob.position
        if (!canSeeHero(gameField, heroPosition, mob.position)) {
            return mob.position
        }
        var bestDistance = getDistance(mob.position, heroPosition)
        var optimalNextPosition = mob.position
        for (x in -1..1) {
            for (y in -1..1) {
                if (x.absoluteValue == y.absoluteValue) {
                    continue
                }
                val nextPosition = Position(mob.position.x + x, mob.position.y + y)
                if (!checkBounds(gameField, nextPosition)) {
                    continue
                }
                val nextCell = gameField.get(nextPosition)
                if (nextCell.groundType.isPassable && nextCell.creature == null) {
                    val newDistance = getDistance(nextPosition, heroPosition)
                    if (newDistance > bestDistance) {
                        bestDistance = newDistance
                        optimalNextPosition = nextPosition
                    }
                }
            }
        }
        return optimalNextPosition
    }

    private fun getDistance(mobPosition: Position, heroPosition: Position): Int {
        return (mobPosition.x - heroPosition.x).absoluteValue + (mobPosition.y - heroPosition.y).absoluteValue
    }

    private fun findHero(gameField: GameField, mobPosition: Position): Position? {
        var x = (mobPosition.x - vision).coerceAtLeast(0)
        val startY = (mobPosition.y - vision).coerceAtLeast(0)
        var y = startY
        while (x <= (mobPosition.x + vision).coerceAtMost(gameField.width - 1)) {
            while (y <= (mobPosition.y + vision).coerceAtMost(gameField.height - 1)) {
                if (gameField.get(x, y).creature is Hero) {
                    return Position(x, y)
                }
                y += 1
            }
            y = startY
            x += 1
        }
        return null
    }

    private fun checkBounds(gameField: GameField, position: Position): Boolean {
        return 0 <= position.x && position.x < gameField.width && 0 <= position.y && position.y < gameField.height
    }
}
