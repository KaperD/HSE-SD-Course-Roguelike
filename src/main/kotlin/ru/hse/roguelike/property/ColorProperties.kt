package ru.hse.roguelike.property

import ru.hse.roguelike.ui.Color
import ru.hse.roguelike.ui.toColor
import java.util.*

/**
 * Определение цветов для элементов игры.
 * Данные загружаются из файла color.properties
 */
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
            load(Color::class.java.getResourceAsStream("/color.properties")!!.reader())
        }
        fun String.loadColor(): Color = properties.getProperty(this).toColor()
        landColor = "color.land".loadColor()
        waterColor = "color.water".loadColor()
        fireColor = "color.fire".loadColor()
        stoneColor = "color.stone".loadColor()
        levelEndColor = "color.level.end".loadColor()
        heroColor = "color.hero".loadColor()
        itemColor = "color.item".loadColor()
        borderColor = "color.border".loadColor()
        titleColor = "color.title".loadColor()
        defaultColor = "color.default".loadColor()
        textColor = "color.text".loadColor()
        highlightColor = "color.highlight".loadColor()
    }
}
