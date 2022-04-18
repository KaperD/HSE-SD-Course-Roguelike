package ru.hse.roguelike.factory.mob

import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.mob.AggressiveMob
import ru.hse.roguelike.model.creature.mob.CowardMob
import ru.hse.roguelike.model.creature.mob.Mob
import ru.hse.roguelike.model.creature.mob.PassiveMob
import ru.hse.roguelike.model.creature.mob.decorator.CloneMobDecorator
import ru.hse.roguelike.property.StringProperties

/**
 * Создает мобов, которые живут в болоте
 */
class SwampMobFactory : MobFactory {

    override fun createCoward(position: Position): Mob {
        val frogHealth = 70
        val frogAttackDamage = 5
        val frogVision = 3
        return CowardMob(
            frogHealth,
            frogHealth,
            frogAttackDamage,
            position,
            frogVision,
            StringProperties.frogDescription
        )
    }

    override fun createAggressive(position: Position): Mob {
        val poisonousMoldHealth = 50
        val poisonousMoldAttackDamage = 5
        val poisonousMoldVision = 4
        return CloneMobDecorator(
            AggressiveMob(
                poisonousMoldHealth,
                poisonousMoldHealth,
                poisonousMoldAttackDamage,
                position,
                poisonousMoldVision,
                StringProperties.poisonousMoldDescription
            )
        )
    }

    override fun createPassive(position: Position): Mob {
        val crocodileHealth = 150
        val crocodileAttackDamage = 30
        return PassiveMob(
            crocodileHealth,
            crocodileHealth,
            crocodileAttackDamage,
            position,
            StringProperties.crocodileDescription
        )
    }
}
