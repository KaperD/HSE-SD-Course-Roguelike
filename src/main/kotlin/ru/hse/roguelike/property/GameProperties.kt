package ru.hse.roguelike.property

import java.util.*

/**
 * Определение параметров игры
 */
interface GameProperties {
    val mapWidth: Int
    val mapHeight: Int
    val imageWidth: Int
    val imageHeight: Int
    val initialHeroHealth: Int
    val initialHeroAttackDamage: Int
    val fireDamage: Int
    val levelsOrder: List<String>
}

/**
 * Определение параметров игры.
 * Данные загружаются из файла game.properties
 */
class GamePropertiesImpl(propertiesFileName: String = "/game.properties") : GameProperties {
    override val mapWidth: Int
    override val mapHeight: Int
    override val imageWidth: Int
    override val imageHeight: Int
    override val initialHeroHealth: Int
    override val initialHeroAttackDamage: Int

    override val fireDamage: Int
    override val levelsOrder: List<String>

    init {
        val properties = Properties().apply {
            load(GameProperties::class.java.getResourceAsStream(propertiesFileName)!!.reader())
        }
        fun String.loadInt(): Int = properties.getProperty(this).toInt()
        mapWidth = "map.width".loadInt()
        mapHeight = "map.height".loadInt()
        imageWidth = "image.width".loadInt()
        imageHeight = "image.height".loadInt()
        initialHeroHealth = "initial.hero.health".loadInt()
        initialHeroAttackDamage = "initial.hero.attack.damage".loadInt()
        fireDamage = "fire.damage".loadInt()
        levelsOrder = properties.getProperty("levels.order").split(",")
    }
}
