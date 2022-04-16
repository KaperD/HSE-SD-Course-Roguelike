package ru.hse.roguelike.model.creature.strategy

import ru.hse.roguelike.model.GameField
import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.mob.Mob

/**
 * Пассивная стратегия:
 * моб всё время стоит на одном месте
 */
class PassiveStrategy : MoveStrategy {
    override fun move(gameField: GameField, mob: Mob): Position {
        return mob.position
    }
}
