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
        linesIterator.next()

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

    private fun readMobs(linesIterator: Iterator<String>): List<Mob> {
        val mobs = mutableListOf<Mob>()
        while (true) {
            val line = linesIterator.next()
            if (line.isBlank()) {
                break
            }
            val split = line.split(whitespaceRegex, mobsSplitSize)
            val x = split[0].toInt()
            val y = split[1].toInt()
            val health = split[2].toInt()
            val attackDamage = split[3].toInt()
            val mobType = split[4]
            val newMob = when (mobType) {
                "coward" -> CowardMob(health, health, attackDamage, Position(x, y), split[5].toInt())
                "aggressive" -> AggressiveMob(health, health, attackDamage, Position(x, y), split[5].toInt())
                "passive" -> PassiveMob(health, health, attackDamage, Position(x, y))
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
