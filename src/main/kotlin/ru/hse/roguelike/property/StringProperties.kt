package ru.hse.roguelike.property

import ru.hse.roguelike.ui.Color
import java.util.*

/**
 * Литералы необходимые для игры.
 * Данные загружаются из файла string.properties
 */
object StringProperties {
    val helpMessage: String

    val makeWindowBigger: String

    val heroStats: String
    val health: String
    val itemsCount: String
    val items: String
    val used: String
    val itemInfo: String
    val itemType: String
    val cellInfo: String
    val cellType: String
    val creatureInfo: String
    val type: String
    val hero: String
    val bonusHealth: String
    val bonusMaximumHealth: String

    val head: String
    val body: String
    val boots: String
    val hands: String
    val weapon: String
    val disposable: String

    val land: String
    val water: String
    val fire: String
    val stone: String
    val levelEnd: String

    val gameOver: String
    val victory: String

    init {
        val properties: Properties = Properties().apply {
            load(Color::class.java.getResourceAsStream("/string.properties")!!.reader())
        }

        fun String.load(): String = properties.getProperty(this)
        helpMessage = "help.message".load()

        makeWindowBigger = "make.window.bigger".load()

        heroStats = "hero.stats".load()
        health = "health".load()
        itemsCount = "items.count".load()
        items = "items".load()
        used = "used".load()
        itemInfo = "item.info".load()
        itemType = "item.type".load()
        cellInfo = "cell.info".load()
        cellType = "cell.type".load()
        creatureInfo = "creature.info".load()
        type = "type".load()
        hero = "hero".load()
        bonusHealth = "bonus.health".load()
        bonusMaximumHealth = "bonus.maximum.health".load()

        head = "head".load()
        body = "body".load()
        boots = "boots".load()
        hands = "hands".load()
        weapon = "weapon".load()
        disposable = "disposable".load()

        land = "land".load()
        water = "water".load()
        fire = "fire".load()
        stone = "stone".load()
        levelEnd = "level.end".load()

        gameOver = "game.over".load()
        victory = "victory".load()
    }
}
