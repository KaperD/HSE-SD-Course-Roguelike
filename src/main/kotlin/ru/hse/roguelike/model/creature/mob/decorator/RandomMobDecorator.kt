package ru.hse.roguelike.model.creature.mob.decorator

import ru.hse.roguelike.model.GameField
import ru.hse.roguelike.model.creature.mob.Mob
import ru.hse.roguelike.model.creature.strategy.MoveStrategy
import ru.hse.roguelike.model.creature.strategy.RandomStrategy

/**
 * Декоратор, который временно заставляет моба передвигаться случайно
 * согласно стратегии RandomStrategy
 *
 * @param baseMob   Моб, на которого будет навешан декоратор
 * @param timeLimit Количество шагов, в пределах которых декоратор будет действовать на моба
 */
class RandomMobDecorator(private val baseMob: Mob, private var timeLimit: Int) : MobDecorator(baseMob) {
    private val oldMoveStrategy: MoveStrategy = baseMob.moveStrategy

    init {
        require(timeLimit > 0) { "Time limit must be positive: $timeLimit" }
        baseMob.moveStrategy = RandomStrategy()
    }

    override fun move(gameField: GameField): List<Mob> {
        val movedMob = super.move(gameField)
        timeLimit -= 1
        return if (timeLimit == 0) {
            movedMob.find { it == baseMob }?.moveStrategy = oldMoveStrategy
            movedMob
        } else {
            movedMob.map { if (it == baseMob) this else it }
        }
    }
}
