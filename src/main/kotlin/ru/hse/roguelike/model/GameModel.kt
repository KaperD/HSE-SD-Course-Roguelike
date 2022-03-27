package ru.hse.roguelike.model

class GameModel(
    var field: GameField,
    val hero: Hero
) {
    init {
//        require(field.get(hero.position).creature == hero)
    }
}
