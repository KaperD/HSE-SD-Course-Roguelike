package ru.hse.roguelike.model.creature.mob.decorator

import ru.hse.roguelike.model.GameField
import ru.hse.roguelike.model.creature.Hero
import ru.hse.roguelike.model.creature.mob.Mob
import ru.hse.roguelike.model.creature.strategy.RandomStrategy

/**
 * Декоратор, который временно заставляет моба передвигаться случайно
 * согласно стратегии RandomStrategy
 *
 * @param baseMob   Моб, на которого будет навешан декоратор
 * @param timeLimit Количество шагов, в пределах которых декоратор будет действовать на моба
 */
class RandomMobDecorator(private val baseMob: Mob, private var timeLimit: Int) : MobDecorator(baseMob) {
    private val randomMoveStrategy = RandomStrategy()

    init {
        require(timeLimit > 0) { "Time limit must be positive: $timeLimit" }
    }

    override fun move(gameField: GameField): List<Mob> {
        timeLimit -= 1
        val newPosition = randomMoveStrategy.move(gameField, this)
        if (newPosition != position) {
            val oldCell = gameField.get(position)
            val newCell = gameField.get(newPosition)
            val newCellCreature = newCell.creature
            if (newCellCreature is Hero) {
                newCellCreature.health -= attackDamage
                health -= newCellCreature.attackDamage
            } else {
                oldCell.creature = null
                newCell.creature = this
                position = newPosition
            }
        }
        return if (timeLimit == 0) {
            listOf(baseMob)
        } else {
            listOf(this)
        }
    }
}
