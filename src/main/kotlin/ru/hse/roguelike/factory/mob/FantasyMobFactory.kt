package ru.hse.roguelike.factory.mob

import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.mob.AggressiveMob
import ru.hse.roguelike.model.creature.mob.CowardMob
import ru.hse.roguelike.model.creature.mob.Mob
import ru.hse.roguelike.model.creature.mob.PassiveMob
import ru.hse.roguelike.property.StringProperties

class FantasyMobFactory : MobFactory {

    override fun createCoward(position: Position): Mob {
        val skeletonHealth = 100
        val skeletonAttackDamage = 10
        val skeletonVision = 4
        return CowardMob(
            skeletonHealth,
            skeletonHealth,
            skeletonAttackDamage,
            position,
            skeletonVision,
            StringProperties.skeletonDescription
        )
    }

    override fun createAggressive(position: Position): Mob {
        val dragonHealth = 150
        val dragonAttackDamage = 16
        val dragonVision = 6
        return AggressiveMob(
            dragonHealth,
            dragonHealth,
            dragonAttackDamage,
            position,
            dragonVision,
            StringProperties.dragonDescription
        )
    }

    override fun createPassive(position: Position): Mob {
        val magicianHealth = 150
        val magicianAttackDamage = 16
        return PassiveMob(
            magicianHealth,
            magicianHealth,
            magicianAttackDamage,
            position,
            StringProperties.magicianDescription
        )
    }
}
