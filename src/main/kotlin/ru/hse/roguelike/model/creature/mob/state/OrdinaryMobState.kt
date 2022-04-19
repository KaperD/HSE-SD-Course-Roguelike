package ru.hse.roguelike.model.creature.mob.state

import ru.hse.roguelike.model.creature.mob.Mob

class OrdinaryMobState(private val healthThreshold: Int) : MobState {

    override fun check(mob: Mob) {
        if (mob.health < healthThreshold) {
            mob.state = PanicMobState(mob, healthThreshold)
        }
    }
}
