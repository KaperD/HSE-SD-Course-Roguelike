package ru.hse.roguelike.property

import ru.hse.roguelike.ui.Color
import java.util.*

object ViewProperties {
    val landSymbol: Char
    val waterSymbol: Char
    val fireSymbol: Char
    val stoneSymbol: Char
    val levelEndSymbol: Char
    val heroSymbol: Char
    val itemSymbol: Char

    init {
        val properties: Properties = Properties().apply {
            load(Color::class.java.getResourceAsStream("/view.properties"))
        }
        fun String.getSymbol(): Char = properties.getProperty(this)[0]
        landSymbol = "symbol.land".getSymbol()
        waterSymbol = "symbol.water".getSymbol()
        fireSymbol = "symbol.fire".getSymbol()
        stoneSymbol = "symbol.stone".getSymbol()
        levelEndSymbol = "symbol.level.end".getSymbol()
        heroSymbol = "symbol.hero".getSymbol()
        itemSymbol = "symbol.item".getSymbol()
    }
}
