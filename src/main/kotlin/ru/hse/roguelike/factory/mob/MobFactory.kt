package ru.hse.roguelike.factory.mob

import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.mob.Mob

/**
 * Абстрактная фабрика для создания мобов
 */
interface MobFactory {

    /**
     * Создать трусливого моба
     *
     * @param position позиция для моба
     */
    fun createCoward(position: Position): Mob

    /**
     * Создать агрессивного моба
     *
     * @param position позиция для моба
     */
    fun createAggressive(position: Position): Mob

    /**
     * Создать пассивного моба
     *
     * @param position позиция для моба
     */
    fun createPassive(position: Position): Mob
}
