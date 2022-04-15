package ru.hse.roguelike.model.creature.strategy

import ru.hse.roguelike.model.GameField
import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.Mob

class PassiveStrategy : MoveStrategy {
    override fun move(gameField: GameField, mob: Mob): Position {
        return mob.position
    }
}
