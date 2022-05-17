package ru.hse.roguelike.model.creature.mob.state

import ru.hse.roguelike.model.creature.mob.Mob

/**
 * Стандартное состояние моба
 * @param healthThreshold - уровень здоровья, при достижении ниже которого моб переходит в паническое состояние
 */
class OrdinaryMobState(private val healthThreshold: Int) : MobState {

    override fun check(mob: Mob) {
        if (mob.health < healthThreshold) {
            mob.state = PanicMobState(mob, healthThreshold)
        }
    }
}
