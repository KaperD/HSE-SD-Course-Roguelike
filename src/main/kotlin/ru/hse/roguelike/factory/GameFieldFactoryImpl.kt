package ru.hse.roguelike.factory

import ru.hse.roguelike.model.Cell
import ru.hse.roguelike.model.GameField
import ru.hse.roguelike.model.GroundType
import ru.hse.roguelike.model.Position
import ru.hse.roguelike.property.ViewProperties.fireSymbol
import ru.hse.roguelike.property.ViewProperties.landSymbol
import ru.hse.roguelike.property.ViewProperties.levelEndSymbol
import ru.hse.roguelike.property.ViewProperties.stoneSymbol
import ru.hse.roguelike.property.ViewProperties.waterSymbol

class GameFieldFactoryImpl(
    private val fieldWidth: Int,
    private val fieldHeight: Int,
    private val itemFactory: ItemFactory,
    private val levelsFolder: String = "levels"
) : GameFieldFactory {

    override fun getByLevelName(name: String): Pair<GameField, Position> {
        val linesIterator =
            GameFieldFactoryImpl::class.java.getResource("/$levelsFolder/$name.txt")!!.readText().lines().iterator()

        val gameField = GameField(readField(linesIterator))
        linesIterator.next()

        readItems(linesIterator, gameField)

        return gameField to readHeroPosition(linesIterator)
    }

    override fun generate(): Pair<GameField, Position> {
        TODO("Not yet implemented")
    }

    private fun readField(linesIterator: Iterator<String>): List<List<Cell>> {
        val field: MutableList<MutableList<Cell>> = mutableListOf()
        repeat(fieldHeight) {
            val line = linesIterator.next()
            require(line.length == fieldWidth) { "Field with and raw length in file mismatch" }
            field.add(line.map { Cell(it.groundType(), mutableListOf(), null) }.toMutableList())
        }
        return field
    }

    private fun readItems(linesIterator: Iterator<String>, gameField: GameField) {
        while (true) {
            val line = linesIterator.next()
            if (line.isBlank()) {
                break
            }
            val split = line.split(whitespaceRegex, itemSplitSize)
            val x = split[0].toInt()
            val y = split[1].toInt()
            val itemId = split[2]
            val item = if (itemId == "?") itemFactory.getRandom() else itemFactory.getById(itemId)
            gameField.get(x, y).items.add(item)
        }
    }

    private fun readHeroPosition(linesIterator: Iterator<String>): Position {
        while (true) {
            val line = linesIterator.next()
            if (line.isNotBlank()) {
                val split = line.split(whitespaceRegex, heroSplitSize)
                val x = split[0].toInt()
                val y = split[1].toInt()
                return Position(x, y)
            }
        }
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
        const val itemSplitSize = 3
        const val heroSplitSize = 2
    }
}
