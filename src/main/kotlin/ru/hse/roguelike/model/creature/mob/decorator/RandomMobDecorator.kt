package ru.hse.roguelike.model.creature.mob.decorator

import ru.hse.roguelike.model.GameField
import ru.hse.roguelike.model.creature.mob.Mob
import ru.hse.roguelike.model.creature.strategy.MoveStrategy
import ru.hse.roguelike.model.creature.strategy.RandomStrategy

class RandomMobDecorator(private val baseMob: Mob, private var timeLimit: Int) : MobDecorator(baseMob) {
    private val oldMoveStrategy: MoveStrategy = baseMob.moveStrategy

    init {
        assert(timeLimit > 0)
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
