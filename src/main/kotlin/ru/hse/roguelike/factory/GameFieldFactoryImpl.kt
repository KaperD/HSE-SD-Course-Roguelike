package ru.hse.roguelike.factory

import ru.hse.roguelike.model.Cell
import ru.hse.roguelike.model.GameField
import ru.hse.roguelike.model.GroundType
import ru.hse.roguelike.property.ViewProperties.fireSymbol
import ru.hse.roguelike.property.ViewProperties.landSymbol
import ru.hse.roguelike.property.ViewProperties.levelEndSymbol
import ru.hse.roguelike.property.ViewProperties.stoneSymbol
import ru.hse.roguelike.property.ViewProperties.waterSymbol
import java.nio.file.Path
import kotlin.io.path.readText

class GameFieldFactoryImpl(
    private val fieldWidth: Int,
    private val fieldHeight: Int,
    private val itemFactory: ItemFactory,
    private val levelsFolder: Path = Path.of("/levels")
) : GameFieldFactory {

    override fun getByLevel(level: Int): GameField {
        val levelTextLines = levelsFolder.resolve("level$level.txt").readText().lines()
        val field: MutableList<MutableList<Cell>> = mutableListOf()
        for (i in 0 until fieldHeight) {
            val line = levelTextLines[i]
            require(line.length == fieldWidth) { "Field with and raw length in file mismatch" }
            field.add(line.map { Cell(it.groundType(), mutableListOf(), null) }.toMutableList())
        }
        val gameField = GameField(field)
        for (i in fieldHeight + 1 until levelTextLines.size) {
            val split = levelTextLines[i].split(whitespaceRegex, 3)
            val x = split[0].toInt()
            val y = split[1].toInt()
            val itemId = split[2]
            val item = if (itemId == "?") itemFactory.getRandom() else itemFactory.getById(itemId)
            gameField.get(x, y).items.add(item)
        }
        return gameField
    }

    override fun generate(): GameField {
        TODO("Not yet implemented")
    }

    private fun Char.groundType(): GroundType = when (this) {
        landSymbol -> GroundType.Land
        waterSymbol -> GroundType.Water
        fireSymbol -> GroundType.Fire
        stoneSymbol -> GroundType.Stone
        levelEndSymbol -> GroundType.LevelEnd
        else -> throw IllegalStateException("Unknown char")
    }

    companion object {
        val whitespaceRegex = "\\s".toRegex()
    }
}
