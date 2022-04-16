package ru.hse.roguelike.model.creature.strategy

import ru.hse.roguelike.model.GameField
import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.Hero
import ru.hse.roguelike.model.creature.mob.Mob
import kotlin.math.absoluteValue

class CowardStrategy(val vision: Int) : MoveStrategy {
    override fun move(gameField: GameField, mob: Mob): Position {
        val heroPosition = findHero(gameField, mob.position) ?: return mob.position
        return if (!canSeeHero(gameField, heroPosition, mob.position)) {
            mob.position
        } else {
            chooseBestNextPosition(gameField, mob.position, heroPosition)
        }
    }

    private fun chooseBestNextPosition(gameField: GameField, mobPosition: Position, heroPosition: Position): Position {
        val bestNextPosition = getAvailableNextPositions(gameField, mobPosition)
            .maxByOrNull { getDistance(it, heroPosition) } ?: mobPosition
        if (getDistance(bestNextPosition, heroPosition) < getDistance(mobPosition, heroPosition)) {
            return mobPosition
        }
        return bestNextPosition
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
}
