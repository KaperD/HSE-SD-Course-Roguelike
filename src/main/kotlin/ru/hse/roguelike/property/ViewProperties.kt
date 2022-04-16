package ru.hse.roguelike.property

import ru.hse.roguelike.ui.Color
import java.util.*

/**
 * Определение символов, которыми отображаются элементы игры на поле.
 * Данные загружаются из файла view.properties
 */
object ViewProperties {
    val landSymbol: Char
    val waterSymbol: Char
    val fireSymbol: Char
    val stoneSymbol: Char
    val levelEndSymbol: Char
    val heroSymbol: Char
    val itemSymbol: Char
    val cowardMobSymbol: Char
    val aggressiveMobSymbol: Char
    val passiveMobSymbol: Char

    init {
        val properties: Properties = Properties().apply {
            load(Color::class.java.getResourceAsStream("/view.properties")!!.reader())
        }
        fun String.loadSymbol(): Char = properties.getProperty(this)[0]
        landSymbol = "symbol.land".loadSymbol()
        waterSymbol = "symbol.water".loadSymbol()
        fireSymbol = "symbol.fire".loadSymbol()
        stoneSymbol = "symbol.stone".loadSymbol()
        levelEndSymbol = "symbol.level.end".loadSymbol()
        heroSymbol = "symbol.hero".loadSymbol()
        itemSymbol = "symbol.item".loadSymbol()
        cowardMobSymbol = "symbol.coward.mob".loadSymbol()
        aggressiveMobSymbol = "symbol.aggressive.mob".loadSymbol()
        passiveMobSymbol = "symbol.passive.mob".loadSymbol()
    }
}
