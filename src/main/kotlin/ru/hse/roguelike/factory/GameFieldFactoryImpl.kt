package ru.hse.roguelike.factory

import com.github.czyzby.noise4j.map.Grid
import com.github.czyzby.noise4j.map.generator.room.dungeon.DungeonGenerator
import ru.hse.roguelike.model.Cell
import ru.hse.roguelike.model.GameField
import ru.hse.roguelike.model.GroundType
import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.mob.AggressiveMob
import ru.hse.roguelike.model.creature.mob.CowardMob
import ru.hse.roguelike.model.creature.mob.Mob
import ru.hse.roguelike.model.creature.mob.PassiveMob
import ru.hse.roguelike.property.ViewProperties.fireSymbol
import ru.hse.roguelike.property.ViewProperties.landSymbol
import ru.hse.roguelike.property.ViewProperties.levelEndSymbol
import ru.hse.roguelike.property.ViewProperties.stoneSymbol
import ru.hse.roguelike.property.ViewProperties.waterSymbol
import kotlin.math.absoluteValue
import kotlin.math.min
import kotlin.random.Random

/**
 * Фабрика, умеющая:
 * - загружать уровни по названию из директории
 * - генерировать уровни
 * @param fieldWidth   Ширина полей, которые будут создаваться
 * @param fieldHeight  Высота полей, которые будут создаваться
 * @param itemFactory  Фабрика для создания предметов
 * @param levelsFolder Директория откуда загружать уровни
 */
class GameFieldFactoryImpl(
    private val fieldWidth: Int,
    private val fieldHeight: Int,
    private val itemFactory: ItemFactory,
    private val levelsFolder: String = "levels"
) : GameFieldFactory {

    /**
     * Получение уровня по названию.
     * В директории с уровнями должен быть файл с именем name
     * @param name название уровня
     * @return игровое поле, мобы и начальная позиция игрока
     */
    override fun getByLevelName(name: String): Triple<GameField, List<Mob>, Position> {
        val linesIterator = GameFieldFactoryImpl::class.java
            .getResource("/$levelsFolder/$name.txt")
            ?.readText()
            ?.lines()
            ?.iterator() ?: throw IllegalStateException("Unknown level $name")

        val gameField = GameField(readField(linesIterator))

        readItems(linesIterator, gameField)

        val mobs = readMobs(linesIterator)
        mobs.forEach {
            gameField.get(it.position).creature = it
        }

        return Triple(gameField, mobs, readHeroPosition(linesIterator))
    }

    /**
     * Генерирование уровня
     * @return игровое поле, мобы и начальная позиция игрока
     */
    override fun generate(): Triple<GameField, List<Mob>, Position> {
        val grid = Grid(fieldWidth, fieldHeight)
        val dungeonGenerator = DungeonGenerator()
        dungeonGenerator.roomGenerationAttempts = 500
        dungeonGenerator.maxRoomSize = (min(fieldWidth, fieldHeight) / 2).let { if (it % 2 == 0) it - 1 else it }
        dungeonGenerator.tolerance = 10
        dungeonGenerator.minRoomSize = 3
        dungeonGenerator.generate(grid)

        val field: MutableList<MutableList<Cell>> = mutableListOf()
        var heroPosition: Position? = null
        val landCellsPositions = mutableListOf<Position>()

        for (y in 0 until grid.height) {
            val row = mutableListOf<Cell>()
            for (x in 0 until grid.width) {
                val groundType = when (grid.get(x, y)) {
                    1.0f -> GroundType.Stone
                    0.5f -> {
                        if (heroPosition == null) {
                            heroPosition = Position(x, y)
                        } else {
                            landCellsPositions.add(Position(x, y))
                        }
                        GroundType.Land
                    }
                    else -> {
                        GroundType.Grass
                    }
                }
                row.add(Cell(groundType, mutableListOf(), null))
            }
            field.add(row)
        }

        landCellsPositions.last().let {
            field[it.y][it.x] = Cell(GroundType.LevelEnd, mutableListOf(), null)
        }
        val gameField = GameField(field)
        val mobs = mutableListOf<Mob>()
        landCellsPositions.shuffle()
        for (position in landCellsPositions.take(10)) {
            val mob = when (Random.nextInt().absoluteValue % 3) {
                0 -> CowardMob(100, 100, 10, position, 6)
                1 -> AggressiveMob(100, 100, 10, position, 6)
                2 -> PassiveMob(100, 100, 10, position)
                else -> throw IllegalStateException("Should not reach here")
            }
            gameField.get(position).creature = mob
            mobs.add(mob)
        }
        repeat(10) {
            val position = landCellsPositions.random()
            gameField.get(position).items.add(itemFactory.getRandom())
        }
        return Triple(gameField, mobs, heroPosition!!)
    }

    private fun readField(linesIterator: Iterator<String>): List<List<Cell>> {
        val field: MutableList<MutableList<Cell>> = mutableListOf()
        while (true) {
            val line = linesIterator.next()
            if (line.trim().startsWith("==")) {
                break
            }
            require(line.length == fieldWidth) { "Field width in file mismatch" }
            field.add(line.map { Cell(it.groundType(), mutableListOf(), null) }.toMutableList())
        }
        require(field.size == fieldHeight) { "Field height in file mismatch" }
        return field
    }

    private fun readItems(linesIterator: Iterator<String>, gameField: GameField) {
        while (true) {
            val line = linesIterator.next()
            if (line.trim().startsWith("==")) {
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

    private fun readMobs(linesIterator: Iterator<String>): List<Mob> {
        val mobs = mutableListOf<Mob>()
        while (true) {
            val line = linesIterator.next()
            if (line.trim().startsWith("==")) {
                break
            }
            val splitIterator = line.split(whitespaceRegex, mobsSplitSize).iterator()
            val x = splitIterator.next().toInt()
            val y = splitIterator.next().toInt()
            val health = splitIterator.next().toInt()
            val attackDamage = splitIterator.next().toInt()
            val newMob = when (val mobType = splitIterator.next()) {
                "coward" -> CowardMob(
                    health,
                    health,
                    attackDamage,
                    Position(x, y),
                    splitIterator.next().toInt()
                )
                "aggressive" -> AggressiveMob(
                    health,
                    health,
                    attackDamage,
                    Position(x, y),
                    splitIterator.next().toInt()
                )
                "passive" -> PassiveMob(
                    health,
                    health,
                    attackDamage,
                    Position(x, y)
                )
                else -> throw IllegalStateException("Unknown mob type $mobType")
            }
            mobs.add(newMob)
        }
        return mobs
    }

    private fun readHeroPosition(linesIterator: Iterator<String>): Position {
        while (true) {
            require(linesIterator.hasNext()) { "Missing hero in level file" }
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
        const val mobsSplitSize = 6
        const val heroSplitSize = 2
    }
}
