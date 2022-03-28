package ru.hse.roguelike.model

import ru.hse.roguelike.model.creature.Hero

class GameModel(
    var field: GameField,
    val hero: Hero
) {
    init {
//        require(field.get(hero.position).creature == hero)
    }
}
