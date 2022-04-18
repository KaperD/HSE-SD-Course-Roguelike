package ru.hse.roguelike.model.creature.mob.decorator

import ru.hse.roguelike.model.GameField
import ru.hse.roguelike.model.creature.mob.Mob
import kotlin.random.Random

/**
 * Декоратор, который при каждом ходе с вероятностью [cloneProbability] клонирует моба
 *
 * @param baseMob Моб, на которого будет навешан декоратор
 */
class CloneMobDecorator(
    private val baseMob: Mob,
    private val cloneProbability: Int = 2,
    seed: Int = Random.nextInt()
) : MobDecorator(baseMob) {
    private val rand = Random(seed)

    override fun move(gameField: GameField): List<Mob> {
        val placesForCopy = listOf(
            position.copy(x = position.x + 1),
            position.copy(x = position.x - 1),
            position.copy(y = position.y + 1),
            position.copy(y = position.y - 1)
        ).filter {
            it.x in 0 until gameField.width &&
                it.y in 0 until gameField.height &&
                gameField.get(it).groundType.isPassable &&
                gameField.get(it).creature == null
        }

        val numberOfPercents = 100
        return if (placesForCopy.isNotEmpty() && rand.nextInt(numberOfPercents) in 0 until cloneProbability) {
            val placeForCopy = placesForCopy.random(rand)
            val newMob = CloneMobDecorator(baseMob.clone().apply { position = placeForCopy })
            listOf(this, newMob)
        } else {
            super.move(gameField).map { if (it == baseMob) this else it }
        }
    }
}
