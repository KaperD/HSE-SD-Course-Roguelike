package ru.hse.roguelike.property

import ru.hse.roguelike.ui.Color
import java.util.*

object StringProperties {
    val helpMessage: String
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
    val makeWindowBigger: String
    val head: String
    val body: String
    val legs: String
    val hands: String
    val weapon: String

    init {
        val properties: Properties = Properties().apply {
            load(Color::class.java.getResourceAsStream("/string.properties"))
        }

        fun String.load(): String = properties.getProperty(this)
        helpMessage = "help.message".load()
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
        makeWindowBigger = "make.window.bigger".load()
        head = "head".load()
        body = "body".load()
        legs = "legs".load()
        hands = "hands".load()
        weapon = "weapon".load()
    }
}
