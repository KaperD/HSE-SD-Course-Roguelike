package ru.hse.roguelike.model.creature.mob.state

import ru.hse.roguelike.model.creature.mob.Mob
import ru.hse.roguelike.model.creature.strategy.RandomStrategy

/**
 * Состояние моба, в котором он делает случайные шаги
 * @param mob - моб, который входит в это состояние
 * @param healthThreshold - уровень здоровья, при достижении которого (или выше) моб переходит в стандартное состояние
 */
class PanicMobState(mob: Mob, private val healthThreshold: Int) : MobState {
    private val oldMoveStrategy = mob.moveStrategy

    init {
        mob.moveStrategy = RandomStrategy()
    }

    override fun check(mob: Mob) {
        if (mob.health >= healthThreshold) {
            mob.moveStrategy = oldMoveStrategy
            mob.state = OrdinaryMobState(healthThreshold)
        }
    }
}
