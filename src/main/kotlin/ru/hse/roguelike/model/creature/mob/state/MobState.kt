package ru.hse.roguelike.model.creature.mob.state

import ru.hse.roguelike.model.creature.mob.Mob

/**
 * Состояние моба, которое определяет его поведение
 */
interface MobState {

    /**
     * Проверяет состояние моба, и если что, меняет его
     */
    fun check(mob: Mob)
}
