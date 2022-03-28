package ru.hse.roguelike.property

import java.util.*

interface SizeProperties {
    val mapWidth: Int
    val mapHeight: Int
    val imageWidth: Int
    val imageHeight: Int
}

class SizePropertiesImpl(propertiesFileName: String = "/size.properties") : SizeProperties {
    override val mapWidth: Int
    override val mapHeight: Int
    override val imageWidth: Int
    override val imageHeight: Int

    init {
        val properties = Properties().apply {
            load(SizeProperties::class.java.getResourceAsStream(propertiesFileName))
        }
        mapWidth = properties.getProperty("map.width").toInt()
        mapHeight = properties.getProperty("map.height").toInt()
        imageWidth = properties.getProperty("image.width").toInt()
        imageHeight = properties.getProperty("image.height").toInt()
    }
}
