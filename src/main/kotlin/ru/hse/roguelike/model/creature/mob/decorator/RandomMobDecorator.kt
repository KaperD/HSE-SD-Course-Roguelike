package ru.hse.roguelike.model.creature.mob.decorator

import ru.hse.roguelike.model.GameField
import ru.hse.roguelike.model.creature.mob.Mob
import ru.hse.roguelike.model.creature.strategy.MoveStrategy
import ru.hse.roguelike.model.creature.strategy.RandomStrategy

class RandomMobDecorator(baseMob: Mob, private var timeLimit: Int) : MobDecorator(baseMob) {
    private val oldMoveStrategy: MoveStrategy = baseMob.moveStrategy

    init {
        require(timeLimit > 0) { "Time limit must be positive: $timeLimit" }
        baseMob.moveStrategy = RandomStrategy()
    }

    override fun move(gameField: GameField): Mob {
        val movedMob = super.move(gameField)
        timeLimit -= 1
        return if (timeLimit == 0) {
            movedMob.moveStrategy = oldMoveStrategy
            movedMob
        } else {
            this
        }
    }
}
