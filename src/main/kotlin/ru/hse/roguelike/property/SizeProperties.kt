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
        fun String.loadInt(): Int = properties.getProperty(this).toInt()
        mapWidth = "map.width".loadInt()
        mapHeight = "map.height".loadInt()
        imageWidth = "image.width".loadInt()
        imageHeight = "image.height".loadInt()
    }
}
