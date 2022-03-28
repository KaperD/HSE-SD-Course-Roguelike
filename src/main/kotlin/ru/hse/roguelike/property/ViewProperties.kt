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
        fun String.loadSymbol(): Char = properties.getProperty(this)[0]
        landSymbol = "symbol.land".loadSymbol()
        waterSymbol = "symbol.water".loadSymbol()
        fireSymbol = "symbol.fire".loadSymbol()
        stoneSymbol = "symbol.stone".loadSymbol()
        levelEndSymbol = "symbol.level.end".loadSymbol()
        heroSymbol = "symbol.hero".loadSymbol()
        itemSymbol = "symbol.item".loadSymbol()
    }
}
