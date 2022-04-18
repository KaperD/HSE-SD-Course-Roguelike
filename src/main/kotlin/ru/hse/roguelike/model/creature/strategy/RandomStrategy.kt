package ru.hse.roguelike.model.creature.strategy

import ru.hse.roguelike.model.GameField
import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.mob.Mob
import kotlin.random.Random

/**
 * Случайная стратегия:
 * моб случайно выбирает следующую позицию из доступных
 * (текущая позиция героя не рассматривается)
 * @param seed Сид для инициализации случайности
 */
class RandomStrategy(seed: Int = Random.nextInt()) : MoveStrategy {
    private val rand = Random(seed)
    override fun move(gameField: GameField, mob: Mob): Position {
        val availableNextPositions = getAvailableNextPositions(gameField, mob.position)
        if (availableNextPositions.isEmpty()) {
            return mob.position
        }
        val chosenPosition = rand.nextInt(availableNextPositions.size)
        return availableNextPositions[chosenPosition]
    }
}
