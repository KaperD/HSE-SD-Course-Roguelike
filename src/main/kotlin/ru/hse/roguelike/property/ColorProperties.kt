package ru.hse.roguelike.property

import ru.hse.roguelike.ui.Color
import ru.hse.roguelike.ui.toColor
import java.util.*

object ColorProperties {
    val landColor: Color
    val waterColor: Color
    val fireColor: Color
    val stoneColor: Color
    val levelEndColor: Color
    val heroColor: Color
    val itemColor: Color
    val borderColor: Color
    val titleColor: Color
    val defaultColor: Color
    val textColor: Color
    val highlightColor: Color

    init {
        val properties: Properties = Properties().apply {
            load(Color::class.java.getResourceAsStream("/color.properties"))
        }
        fun String.getColor(): Color = properties.getProperty(this).toColor()
        landColor = "color.land".getColor()
        waterColor = "color.water".getColor()
        fireColor = "color.fire".getColor()
        stoneColor = "color.stone".getColor()
        levelEndColor = "color.level.end".getColor()
        heroColor = "color.hero".getColor()
        itemColor = "color.item".getColor()
        borderColor = "color.border".getColor()
        titleColor = "color.title".getColor()
        defaultColor = "color.default".getColor()
        textColor = "color.text".getColor()
        highlightColor = "color.highlight".getColor()
    }
}
