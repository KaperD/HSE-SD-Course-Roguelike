package ru.hse.roguelike.model

import com.github.czyzby.noise4j.map.Grid
import com.github.czyzby.noise4j.map.generator.room.dungeon.DungeonGenerator
import ru.hse.roguelike.factory.item.ItemFactory
import ru.hse.roguelike.factory.mob.MobFactory
import ru.hse.roguelike.model.creature.mob.Mob
import ru.hse.roguelike.property.ViewProperties
import kotlin.math.absoluteValue
import kotlin.math.min
import kotlin.random.Random

/**
 * Прямоугольное игровое поле, состоящее из конечного числа клеток
 * @param field набор клеток игрового поля
 */
class GameField(
    private val field: List<List<Cell>>
) {
    /**
     * Размер поля по горизонтали
     */
    val width = field.firstOrNull()?.size ?: 0

    /**
     * Размер поля по вертикали
     */
    val height = field.size

    /**
     * Доступ к клетке
     * @param x координата по горизонтали
     * @param y координата по вертикали
     * @return клетка с координатами x, y
     */
    fun get(x: Int, y: Int): Cell = field[y][x]

    /**
     * Доступ к клетке
     * @param position позиция клетке
     * @return клетка с позицией position
     */
    fun get(position: Position): Cell = field[position.y][position.x]

    companion object {
        /**
         * Builder игрового поля
         */
        fun builder() = FieldBuilder()
    }
}

/**
 * Builder игрового поля
 */
class FieldBuilder {
    /**
     * Сгенерировать случайное игровое поле
     */
    fun generateRandom() = RandomFieldBuilder()

    /**
     * Загрузить поле из файла
     */
    fun loadFromFile(levelName: String) = FileFieldBuilder(levelName)
}

/**
 * Базовый builder игрового поля
 */
sealed class BaseFieldBuilder {
    protected var width: Int? = null
    protected var height: Int? = null
    protected var itemFactory: ItemFactory? = null
    protected var mobFactory: MobFactory? = null

    /**
     * Ширина поля, которое будет создано
     */
    open fun withWidth(width: Int): BaseFieldBuilder = apply {
        require(width > 0) { "Field width must be positive" }
        this.width = width
    }

    /**
     * Высота поля, которое будет создано
     */
    open fun withHeight(height: Int): BaseFieldBuilder = apply {
        require(height > 0) { "Field height must be positive" }
        this.height = height
    }

    /**
     * Фабрика для создания предметов
     */
    open fun withItemFactory(itemFactory: ItemFactory): BaseFieldBuilder = apply {
        this.itemFactory = itemFactory
    }

    /**
     * Фабрика для создания мобов
     */
    open fun withMobFactory(mobFactory: MobFactory): BaseFieldBuilder = apply {
        this.mobFactory = mobFactory
    }

    abstract fun build(): Triple<GameField, List<Mob>, Position>
}

/**
 * Генерирование случайного уровня
 */
class RandomFieldBuilder : BaseFieldBuilder() {
    private var numberOfMobs: Int? = null
    private var numberOfItems: Int? = null

    /**
     * Число мобов, которые будут случайно разбросаны по уровню
     */
    fun withNumberOfMobs(numberOfMobs: Int) = apply {
        require(numberOfMobs >= 0) { "Number of mobs should be non negative" }
        this.numberOfMobs = numberOfMobs
    }

    /**
     * Число предметов, которые будут случайно разбросаны по уровню
     */
    fun withNumberOfItems(numberOfItems: Int) = apply {
        require(numberOfItems >= 0) { "Number of mobs should be non negative" }
        this.numberOfItems = numberOfItems
    }

    /**
     * Ширина поля, которое будет создано
     */
    override fun withWidth(width: Int) = apply { super.withWidth(width) }

    /**
     * Высота поля, которое будет создано
     */
    override fun withHeight(height: Int) = apply { super.withHeight(height) }

    /**
     * Фабрика для создания предметов
     */
    override fun withItemFactory(itemFactory: ItemFactory) = apply { super.withItemFactory(itemFactory) }

    /**
     * Фабрика для создания мобов
     */
    override fun withMobFactory(mobFactory: MobFactory) = apply { super.withMobFactory(mobFactory) }

    override fun build(): Triple<GameField, List<Mob>, Position> {
        val width = requireNotNull(width) { "Width must be set" }
        val height = requireNotNull(height) { "Height must be set" }
        val itemFactory = requireNotNull(itemFactory) { "Item factory must be set" }
        val mobFactory = requireNotNull(mobFactory) { "Mob factory must be set" }
        val numberOfMobs = requireNotNull(numberOfMobs) { "Number of mobs must be set" }
        val numberOfItems = requireNotNull(numberOfItems) { "Number of items must be set" }

        val roomGenerationAttempts = 500
        val tolerance = 10
        val minRoomSize = 3

        val grid = Grid(width, height)
        val dungeonGenerator = DungeonGenerator()
        dungeonGenerator.roomGenerationAttempts = roomGenerationAttempts
        dungeonGenerator.maxRoomSize = (min(width, height) / 2).let { if (it % 2 == 0) it - 1 else it }
        dungeonGenerator.tolerance = tolerance
        dungeonGenerator.minRoomSize = minRoomSize
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
        for (position in landCellsPositions.take(numberOfMobs)) {
            val mob = when (Random.nextInt().absoluteValue % 3) {
                0 -> mobFactory.createCoward(position)
                1 -> mobFactory.createAggressive(position)
                2 -> mobFactory.createPassive(position)
                else -> throw IllegalStateException("Should not reach here")
            }
            gameField.get(position).creature = mob
            mobs.add(mob)
        }
        repeat(numberOfItems) {
            val position = landCellsPositions.random()
            gameField.get(position).items.add(itemFactory.getRandom())
        }
        return Triple(gameField, mobs, heroPosition!!)
    }
}

/**
 * Строитель уровня по названию.
 * В директории с уровнями должен быть файл с именем levelName
 * @param levelName название уровня
 */
class FileFieldBuilder(private val levelName: String) : BaseFieldBuilder() {
    private val levelsFolder: String = "levels"

    /**
     * Ширина поля, которое будет создано
     */
    override fun withWidth(width: Int) = apply { super.withWidth(width) }

    /**
     * Высота поля, которое будет создано
     */
    override fun withHeight(height: Int) = apply { super.withHeight(height) }

    /**
     * Фабрика для создания предметов
     */
    override fun withItemFactory(itemFactory: ItemFactory) = apply { super.withItemFactory(itemFactory) }

    /**
     * Фабрика для создания мобов
     */
    override fun withMobFactory(mobFactory: MobFactory) = apply { super.withMobFactory(mobFactory) }

    override fun build(): Triple<GameField, List<Mob>, Position> {
        val width = requireNotNull(width) { "Width must be set" }
        val height = requireNotNull(height) { "Height must be set" }
        val itemFactory = requireNotNull(itemFactory) { "Item factory must be set" }
        val mobFactory = requireNotNull(mobFactory) { "Mob factory must be set" }

        val linesIterator = FileFieldBuilder::class.java
            .getResource("/$levelsFolder/$levelName.txt")
            ?.readText()
            ?.lines()
            ?.iterator() ?: throw IllegalStateException("Unknown level $levelName")

        val gameField = GameField(readField(linesIterator, width, height))

        readItems(linesIterator, gameField, itemFactory)

        val mobs = readMobs(linesIterator, mobFactory)
        mobs.forEach {
            gameField.get(it.position).creature = it
        }

        return Triple(gameField, mobs, readHeroPosition(linesIterator))
    }

    private fun readField(linesIterator: Iterator<String>, fieldWidth: Int, fieldHeight: Int): List<List<Cell>> {
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

    private fun readItems(linesIterator: Iterator<String>, gameField: GameField, itemFactory: ItemFactory) {
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

    private fun readMobs(linesIterator: Iterator<String>, mobFactory: MobFactory): List<Mob> {
        val mobs = mutableListOf<Mob>()
        while (true) {
            val line = linesIterator.next()
            if (line.trim().startsWith("==")) {
                break
            }
            val split = line.split(whitespaceRegex, mobsSplitSize)
            val position = Position(split[0].toInt(), split[1].toInt())
            val newMob = when (val mobType = split[2]) {
                "coward" -> mobFactory.createCoward(position)
                "aggressive" -> mobFactory.createAggressive(position)
                "passive" -> mobFactory.createPassive(position)
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
        ViewProperties.landSymbol -> GroundType.Land
        ViewProperties.waterSymbol -> GroundType.Water
        ViewProperties.fireSymbol -> GroundType.Fire
        ViewProperties.stoneSymbol -> GroundType.Stone
        ViewProperties.levelEndSymbol -> GroundType.LevelEnd
        else -> throw IllegalStateException("Unknown char")
    }

    companion object {
        val whitespaceRegex = "\\s".toRegex()
        const val itemSplitSize = 3
        const val mobsSplitSize = 3
        const val heroSplitSize = 2
    }
}
