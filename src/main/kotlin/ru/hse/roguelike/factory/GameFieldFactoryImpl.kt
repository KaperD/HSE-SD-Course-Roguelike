package ru.hse.roguelike.factory

import ru.hse.roguelike.model.Cell
import ru.hse.roguelike.model.GameField
import ru.hse.roguelike.model.GroundType
import ru.hse.roguelike.model.Position
import ru.hse.roguelike.model.creature.AggressiveMob
import ru.hse.roguelike.model.creature.CowardMob
import ru.hse.roguelike.model.creature.Mob
import ru.hse.roguelike.model.creature.PassiveMob
import ru.hse.roguelike.property.ViewProperties.fireSymbol
import ru.hse.roguelike.property.ViewProperties.landSymbol
import ru.hse.roguelike.property.ViewProperties.levelEndSymbol
import ru.hse.roguelike.property.ViewProperties.stoneSymbol
import ru.hse.roguelike.property.ViewProperties.waterSymbol

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
     * @return игровое поле и начальная позиция игрока
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
     * @return игровое поле и начальная позиция игрока
     */
    override fun generate(): Triple<GameField, List<Mob>, Position> {
        TODO("Not yet implemented")
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
