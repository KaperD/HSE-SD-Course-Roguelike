package ru.hse.roguelike.model.creature.strategy

import ru.hse.roguelike.model.GameField
import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.Hero
import ru.hse.roguelike.model.creature.Mob
import java.util.*

class AggressiveStrategy(val vision: Int) : MoveStrategy {
    private val used: MutableList<MutableList<Boolean>> =
        MutableList(2 * vision + 1) { MutableList(2 * vision + 1) { mark } }
    private val prev: MutableList<MutableList<Position?>> =
        MutableList(2 * vision + 1) { MutableList(2 * vision + 1) { null } }
    private var mark = false

    override fun move(gameField: GameField, mob: Mob): Position {
        val bfsResult = findBestNextPosition(gameField, mob.position) ?: return mob.position
        if (!canSeeHero(gameField, bfsResult.heroPosition, mob.position)) {
            return mob.position
        }
        return bfsResult.bestNextPosition
    }

    private fun findBestNextPosition(gameField: GameField, mobPosition: Position): BFSResult? {
        mark = !mark
        val queue: Queue<Position> = LinkedList()
        queue.offer(mobPosition)
        setPrev(mobPosition, mobPosition, null)
        markAsUsed(mobPosition, mobPosition)
        while (queue.isNotEmpty()) {
            val curPosition = queue.poll()
            for (nextPosition in getAvailableNextPositions(gameField, curPosition)) {
                if (checkVisionBounds(gameField, mobPosition, nextPosition) &&
                    getMark(nextPosition, mobPosition) != mark
                ) {
                    val nextCell = gameField.get(nextPosition)
                    if (nextCell.creature is Hero) {
                        if (getPrev(curPosition, mobPosition) == null) {
                            return BFSResult(nextPosition, nextPosition)
                        }
                        val bestNextPosition = recoverBestNextPosition(curPosition, mobPosition)
                        return BFSResult(bestNextPosition, nextPosition)
                    }
                    if (nextCell.groundType.isPassable && nextCell.creature == null) {
                        markAsUsed(nextPosition, mobPosition)
                        setPrev(nextPosition, mobPosition, curPosition)
                        queue.offer(nextPosition)
                    }
                }
            }
        }
        return null
    }

    private fun recoverBestNextPosition(prevHeroPosition: Position, mobPosition: Position): Position {
        var pos = prevHeroPosition
        while (getPrev(getPrev(pos, mobPosition)!!, mobPosition) != null) {
            pos = getPrev(pos, mobPosition)!!
        }
        return pos
    }

    private fun getPrev(position: Position, mobPosition: Position): Position? {
        return prev[position.x - mobPosition.x + vision][position.y - mobPosition.y + vision]
    }

    private fun getMark(position: Position, mobPosition: Position): Boolean {
        return used[position.x - mobPosition.x + vision][position.y - mobPosition.y + vision]
    }

    private fun markAsUsed(position: Position, mobPosition: Position) {
        used[position.x - mobPosition.x + vision][position.y - mobPosition.y + vision] = mark
    }

    private fun setPrev(position: Position, mobPosition: Position, prevPosition: Position?) {
        prev[position.x - mobPosition.x + vision][position.y - mobPosition.y + vision] = prevPosition
    }

    private fun checkVisionBounds(gameField: GameField, mobPosition: Position, position: Position): Boolean {
        return (mobPosition.x - vision).coerceAtLeast(0) <= position.x &&
            position.x <= (mobPosition.x + vision).coerceAtMost(gameField.width - 1) &&
            (mobPosition.y - vision).coerceAtLeast(0) <= position.y &&
            position.y <= (mobPosition.y + vision).coerceAtMost(gameField.height - 1)
    }

    private companion object {
        data class BFSResult(
            val bestNextPosition: Position,
            val heroPosition: Position
        )
    }
}
