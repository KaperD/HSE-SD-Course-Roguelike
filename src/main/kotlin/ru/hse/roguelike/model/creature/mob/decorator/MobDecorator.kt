package ru.hse.roguelike.model.creature.mob.decorator

import ru.hse.roguelike.model.creature.mob.Mob

abstract class MobDecorator(baseMob: Mob) : Mob by baseMob
