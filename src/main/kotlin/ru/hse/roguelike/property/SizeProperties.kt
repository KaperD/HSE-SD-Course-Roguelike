package ru.hse.roguelike.property

import java.util.*

class SizeProperties {
    val mapWidth: Int
    val mapHeight: Int
    val imageWidth: Int
    val imageHeight: Int

    init {
        val properties = Properties().apply {
            load(SizeProperties::class.java.getResourceAsStream("/application.properties"))
        }
        mapWidth = properties.getProperty("map.width").toInt()
        mapHeight = properties.getProperty("map.height").toInt()
        imageWidth = properties.getProperty("image.width").toInt()
        imageHeight = properties.getProperty("image.height").toInt()
    }
}
