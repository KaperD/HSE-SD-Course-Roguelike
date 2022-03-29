package ru.hse.roguelike.property

import java.util.*

interface GameProperties {
    val mapWidth: Int
    val mapHeight: Int
    val imageWidth: Int
    val imageHeight: Int
    val initialHeroHealth: Int
    val fireDamage: Int
}

class GamePropertiesImpl(propertiesFileName: String = "/game.properties") : GameProperties {
    override val mapWidth: Int
    override val mapHeight: Int
    override val imageWidth: Int
    override val imageHeight: Int
    override val initialHeroHealth: Int
    override val fireDamage: Int

    init {
        val properties = Properties().apply {
            load(GameProperties::class.java.getResourceAsStream(propertiesFileName))
        }
        fun String.loadInt(): Int = properties.getProperty(this).toInt()
        mapWidth = "map.width".loadInt()
        mapHeight = "map.height".loadInt()
        imageWidth = "image.width".loadInt()
        imageHeight = "image.height".loadInt()
        initialHeroHealth = "initial.hero.health".loadInt()
        fireDamage = "fire.damage".loadInt()
    }
}
