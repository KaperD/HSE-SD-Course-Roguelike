package ru.hse.roguelike.model.creature.mob.decorator

import ru.hse.roguelike.model.creature.mob.Mob

/**
 * Декоратор, который можно навесить на моба
 * @param baseMob Моб, на которого будет навешан декоратор
 */
abstract class MobDecorator(baseMob: Mob) : Mob by baseMob
