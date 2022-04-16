package ru.hse.roguelike.model.creature.strategy

import ru.hse.roguelike.model.GameField
import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.mob.Mob
import kotlin.random.Random

class RandomStrategy : MoveStrategy {
    override fun move(gameField: GameField, mob: Mob): Position {
        val availableNextPositions = getAvailableNextPositions(gameField, mob.position)
        if (availableNextPositions.isEmpty()) {
            return mob.position
        }
        val chosenPosition = Random.nextInt(availableNextPositions.size)
        return availableNextPositions[chosenPosition]
    }
}
