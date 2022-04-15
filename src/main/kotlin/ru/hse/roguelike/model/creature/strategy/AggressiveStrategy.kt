package ru.hse.roguelike.model.creature.strategy

import ru.hse.roguelike.model.GameField
import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.Hero
import ru.hse.roguelike.model.creature.Mob
import java.util.*
import kotlin.math.absoluteValue

class AggressiveStrategy(val vision: Int) : MoveStrategy {
    private val used: MutableList<MutableList<Boolean>> = mutableListOf()
    private val prev: MutableList<MutableList<Position?>> = mutableListOf()
    private var mark = false

    init {
        for (i in 0 until 2 * vision + 1) {
            used.add(MutableList(2 * vision + 1) { mark })
            prev.add(MutableList(2 * vision + 1) { Position(0, 0) })
        }
    }

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
        used[mobPosition.x][mobPosition.y] = mark
        prev[mobPosition.x][mobPosition.y] = null
        while (queue.isNotEmpty()) {
            val curPosition = queue.poll()
            for (x in -1..1) {
                for (y in -1..1) {
                    if (x.absoluteValue == y.absoluteValue) {
                        continue
                    }
                    val nextPosition = Position(curPosition.x + x, curPosition.y + y)
                    if (checkBounds(gameField, mobPosition, nextPosition) &&
                        used[nextPosition.x][nextPosition.y] != mark
                    ) {
                        if (gameField.get(nextPosition).creature is Hero) {
                            if (getPrev(curPosition) == null) {
                                return BFSResult(nextPosition, nextPosition)
                            }
                            val bestNextPosition = recoverBestNextPosition(curPosition)
                            return BFSResult(bestNextPosition, nextPosition)
                        }
                        if (gameField.get(nextPosition).groundType.isPassable &&
                            gameField.get(nextPosition).creature == null
                        ) {
                            used[nextPosition.x][nextPosition.y] = mark
                            prev[nextPosition.x][nextPosition.y] = curPosition
                            queue.offer(nextPosition)
                        }
                    }
                }
            }
        }
        return null
    }

    private fun recoverBestNextPosition(prevHeroPosition: Position): Position {
        var pos = prevHeroPosition
        while (getPrev(getPrev(pos)!!) != null) {
            pos = getPrev(pos)!!
        }
        return pos
    }

    private fun getPrev(position: Position): Position? {
        return prev[position.x][position.y]
    }

    private fun checkBounds(gameField: GameField, mobPosition: Position, position: Position): Boolean {
        return (mobPosition.x - vision).coerceAtLeast(0) <= position.x &&
            position.x <= (mobPosition.x + vision).coerceAtMost(gameField.width - 1) &&
            (mobPosition.y - vision).coerceAtLeast(0) <= position.y &&
            position.y <= (mobPosition.y + vision).coerceAtMost(gameField.height - 1)
    }
}

data class BFSResult(
    val bestNextPosition: Position,
    val heroPosition: Position
)
