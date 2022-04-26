package ru.hse.roguelike.state

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import ru.hse.roguelike.factory.GameFieldFactoryImpl
import ru.hse.roguelike.factory.ItemFactoryImpl
import ru.hse.roguelike.input.InputType
import ru.hse.roguelike.model.*
import ru.hse.roguelike.model.creature.Hero
import ru.hse.roguelike.model.creature.mob.AggressiveMob
import ru.hse.roguelike.model.creature.mob.CowardMob
import ru.hse.roguelike.model.creature.mob.PassiveMob
import ru.hse.roguelike.model.creature.mob.decorator.RandomMobDecorator
import ru.hse.roguelike.property.GameProperties
import ru.hse.roguelike.property.GamePropertiesImpl
import ru.hse.roguelike.property.StateProperties
import ru.hse.roguelike.sound.GameSound
import ru.hse.roguelike.ui.map.MapView
import kotlin.test.*

internal class MapStateTest {

    @Test
    fun `test map mode victory`() {
        val hero = Hero(100, 100, 10, Position(0, 0), mutableListOf())
        val initField = GameField(listOf())
        val gameModel = GameModel(initField, mutableListOf(), hero)
        val mapView = mockk<MapView>(relaxed = true)
        val gameSound = mockk<GameSound>(relaxed = true)
        val anotherState = mockk<State>()
        val gameFieldFactory = GameFieldFactoryImpl(2, 2, ItemFactoryImpl())
        val gameProperties = GamePropertiesImpl()
        val gameOverState = mockk<State>()
        val victoryState = mockk<State>()
        val mapState = MapState(
            gameModel,
            mapView,
            gameSound,
            mapOf(InputType.I to anotherState),
            gameFieldFactory,
            gameProperties,
            gameOverState,
            victoryState
        )

        val aField = gameModel.field
        assertEquals(GroundType.Fire, aField.get(0, 1).groundType)

        mapState.activate()
        verify { mapView.set(0, 0, aField.get(0, 0)) }
        verify { mapView.set(0, 1, aField.get(0, 1)) }
        verify { mapView.set(1, 0, aField.get(1, 0)) }
        verify { mapView.set(1, 1, aField.get(1, 1)) }
        verify { mapView.setHeroStats(hero) }
        verify { mapView.show() }
        confirmVerified(mapView)

        assertEquals(mapState, mapState.handleInput(StateProperties.moveDown))
        verify { mapView.set(0, 0, aField.get(0, 0)) }
        verify { mapView.set(0, 1, aField.get(0, 1)) }
        verify { mapView.set(1, 0, aField.get(1, 0)) }
        verify { mapView.set(1, 1, aField.get(1, 1)) }
        verify { mapView.setHeroStats(hero) }
        verify { mapView.show() }
        confirmVerified(mapView)
        assertEquals(Position(0, 1), hero.position)

        assertEquals(mapState, mapState.handleInput(StateProperties.moveRight))
        val bField = gameModel.field
        assertEquals(GroundType.Land, bField.get(0, 1).groundType)
        verify { mapView.set(0, 1, aField.get(0, 1)) }
        verify { mapView.set(1, 1, aField.get(1, 1)) }
        verify { mapView.set(0, 0, bField.get(0, 0)) }
        verify { mapView.set(0, 1, bField.get(0, 1)) }
        verify { mapView.set(1, 0, bField.get(1, 0)) }
        verify { mapView.set(1, 1, bField.get(1, 1)) }
        verify { mapView.setHeroStats(hero) }
        verify { mapView.show() }
        confirmVerified(mapView)
        assertEquals(Position(0, 1), hero.position)

        assertEquals(victoryState, mapState.handleInput(StateProperties.moveRight))
        verify { mapView.set(0, 1, bField.get(0, 1)) }
        verify { mapView.set(1, 1, bField.get(1, 1)) }
        verify { mapView.show() }
        confirmVerified(mapView)
    }

    @Test
    fun `test map mode game over`() {
        val hero = Hero(1, 100, 10, Position(0, 0), mutableListOf())
        val initField = GameField(listOf())
        val gameModel = GameModel(initField, mutableListOf(), hero)
        val mapView = mockk<MapView>(relaxed = true)
        val gameSound = mockk<GameSound>(relaxed = true)
        val anotherState = mockk<State>()
        val gameFieldFactory = GameFieldFactoryImpl(2, 2, ItemFactoryImpl())
        val gameProperties = mockk<GameProperties>()
        every { gameProperties.fireDamage } returns 1
        every { gameProperties.levelsOrder } returns listOf("a")
        val gameOverState = mockk<State>()
        val victoryState = mockk<State>()
        val mapState = MapState(
            gameModel,
            mapView,
            gameSound,
            mapOf(InputType.I to anotherState),
            gameFieldFactory,
            gameProperties,
            gameOverState,
            victoryState
        )

        val aField = gameModel.field

        mapState.activate()
        verify { mapView.set(0, 0, aField.get(0, 0)) }
        verify { mapView.set(0, 1, aField.get(0, 1)) }
        verify { mapView.set(1, 0, aField.get(1, 0)) }
        verify { mapView.set(1, 1, aField.get(1, 1)) }
        verify { mapView.setHeroStats(hero) }
        verify { mapView.show() }
        confirmVerified(mapView)

        assertEquals(gameOverState, mapState.handleInput(StateProperties.moveDown))
        verify { mapView.set(0, 0, aField.get(0, 0)) }
        verify { mapView.set(0, 1, aField.get(0, 1)) }
        verify { mapView.set(1, 0, aField.get(1, 0)) }
        verify { mapView.set(1, 1, aField.get(1, 1)) }
        verify { mapView.setHeroStats(hero) }
        verify { mapView.show() }
        confirmVerified(mapView)
        assertEquals(Position(0, 1), hero.position)
    }

    @Test
    fun `test map mode fire damage`() {
        val initialHeroHealth = 100
        val fireDamage = 10
        val stepsOnFire = 9

        val hero = Hero(initialHeroHealth, 100, 10, Position(0, 0), mutableListOf())
        val initField = GameField(listOf())
        val gameModel = GameModel(initField, mutableListOf(), hero)
        val mapView = mockk<MapView>(relaxed = true)
        val gameSound = mockk<GameSound>(relaxed = true)
        val gameFieldFactory = GameFieldFactoryImpl(2, 2, ItemFactoryImpl())
        val gameProperties = mockk<GameProperties>()
        every { gameProperties.fireDamage } returns fireDamage
        every { gameProperties.levelsOrder } returns listOf("a")
        val gameOverState = mockk<State>()
        val victoryState = mockk<State>()
        val mapState = MapState(
            gameModel,
            mapView,
            gameSound,
            mapOf(),
            gameFieldFactory,
            gameProperties,
            gameOverState,
            victoryState
        )

        mapState.activate()
        assertEquals(Position(0, 0), hero.position)

        for (step in 1..stepsOnFire) {
            assertEquals(mapState, mapState.handleInput(StateProperties.moveDown))
            assertEquals(Position(0, 1), hero.position)
            assertEquals(mapState, mapState.handleInput(StateProperties.moveUp))
            assertEquals(Position(0, 0), hero.position)
            assertEquals(initialHeroHealth - step * fireDamage, hero.health)
        }
    }

    @Test
    fun `test map mode borders`() {
        val hero = Hero(100, 100, 10, Position(0, 0), mutableListOf())
        val initField = GameField(listOf())
        val gameModel = GameModel(initField, mutableListOf(), hero)
        val mapView = mockk<MapView>(relaxed = true)
        val gameSound = mockk<GameSound>(relaxed = true)
        val gameFieldFactory = GameFieldFactoryImpl(2, 2, ItemFactoryImpl())
        val gameProperties = mockk<GameProperties>(relaxed = true)
        every { gameProperties.levelsOrder } returns listOf("empty_land")
        val gameOverState = mockk<State>()
        val victoryState = mockk<State>()
        val mapState = MapState(
            gameModel,
            mapView,
            gameSound,
            mapOf(),
            gameFieldFactory,
            gameProperties,
            gameOverState,
            victoryState
        )

        mapState.activate()
        assertEquals(Position(0, 0), hero.position)

        assertEquals(mapState, mapState.handleInput(StateProperties.moveLeft))
        assertEquals(Position(0, 0), hero.position)
        verify { gameSound.beep() }
        confirmVerified(gameSound)

        assertEquals(mapState, mapState.handleInput(StateProperties.moveUp))
        assertEquals(Position(0, 0), hero.position)
        verify { gameSound.beep() }
        confirmVerified(gameSound)

        // (0, 0) -> (0, 1)

        assertEquals(mapState, mapState.handleInput(StateProperties.moveDown))
        assertEquals(Position(0, 1), hero.position)

        assertEquals(mapState, mapState.handleInput(StateProperties.moveLeft))
        assertEquals(Position(0, 1), hero.position)
        verify { gameSound.beep() }
        confirmVerified(gameSound)

        assertEquals(mapState, mapState.handleInput(StateProperties.moveDown))
        assertEquals(Position(0, 1), hero.position)
        verify { gameSound.beep() }
        confirmVerified(gameSound)

        // (0, 1) -> (1, 1)

        assertEquals(mapState, mapState.handleInput(StateProperties.moveRight))
        assertEquals(Position(1, 1), hero.position)

        assertEquals(mapState, mapState.handleInput(StateProperties.moveRight))
        assertEquals(Position(1, 1), hero.position)
        verify { gameSound.beep() }
        confirmVerified(gameSound)

        assertEquals(mapState, mapState.handleInput(StateProperties.moveDown))
        assertEquals(Position(1, 1), hero.position)
        verify { gameSound.beep() }
        confirmVerified(gameSound)

        // (1, 1) -> (1, 0)

        assertEquals(mapState, mapState.handleInput(StateProperties.moveUp))
        assertEquals(Position(1, 0), hero.position)

        assertEquals(mapState, mapState.handleInput(StateProperties.moveRight))
        assertEquals(Position(1, 0), hero.position)
        verify { gameSound.beep() }
        confirmVerified(gameSound)

        assertEquals(mapState, mapState.handleInput(StateProperties.moveUp))
        assertEquals(Position(1, 0), hero.position)
        verify { gameSound.beep() }
        confirmVerified(gameSound)
    }

    @Test
    fun `test map mode take items`() {
        val hero = Hero(100, 100, 10, Position(0, 0), mutableListOf())
        val initField = GameField(listOf())
        val gameModel = GameModel(initField, mutableListOf(), hero)
        val mapView = mockk<MapView>(relaxed = true)
        val gameSound = mockk<GameSound>(relaxed = true)
        val itemFactory = ItemFactoryImpl()
        val gameFieldFactory = GameFieldFactoryImpl(2, 2, itemFactory)
        val gameProperties = mockk<GameProperties>(relaxed = true)
        every { gameProperties.levelsOrder } returns listOf("a")
        val gameOverState = mockk<State>()
        val victoryState = mockk<State>()
        val mapState = MapState(
            gameModel,
            mapView,
            gameSound,
            mapOf(),
            gameFieldFactory,
            gameProperties,
            gameOverState,
            victoryState
        )

        mapState.activate()
        assertEquals(Position(0, 0), hero.position)

        assertEquals(mapState, mapState.handleInput(StateProperties.moveDown))
        assertEquals(Position(0, 1), hero.position)
        assertContentEquals(listOf(itemFactory.getById("healing_salve")), hero.items)

        assertEquals(mapState, mapState.handleInput(StateProperties.moveUp))
        assertEquals(Position(0, 0), hero.position)

        assertEquals(mapState, mapState.handleInput(StateProperties.moveDown))
        assertEquals(Position(0, 1), hero.position)

        assertEquals(mapState, mapState.handleInput(StateProperties.moveDown))
        assertEquals(Position(0, 1), hero.position)
        assertContentEquals(listOf(itemFactory.getById("healing_salve")), hero.items)
    }

    @Test
    fun `test map mode mobs moves`() {
        val hero = Hero(100, 100, 10, Position(0, 0), mutableListOf())
        val initField = GameField(listOf())
        val gameModel = GameModel(initField, mutableListOf(), hero)
        val mapView = mockk<MapView>(relaxed = true)
        val gameSound = mockk<GameSound>(relaxed = true)
        val itemFactory = ItemFactoryImpl()
        val gameFieldFactory = GameFieldFactoryImpl(4, 3, itemFactory)
        val gameProperties = mockk<GameProperties>(relaxed = true)
        every { gameProperties.levelsOrder } returns listOf("with_mobs")
        every { gameProperties.confusionTime } returns 0
        val gameOverState = mockk<State>()
        val victoryState = mockk<State>()
        val mapState = MapState(
            gameModel,
            mapView,
            gameSound,
            mapOf(),
            gameFieldFactory,
            gameProperties,
            gameOverState,
            victoryState
        )

        mapState.activate()
        assertEquals(Position(0, 0), hero.position)

        val aggressive = gameModel.mobs[0] as AggressiveMob
        assertEquals(Position(3, 0), aggressive.position)

        val coward = gameModel.mobs[1] as CowardMob
        assertEquals(Position(0, 1), coward.position)

        val passive = gameModel.mobs[2] as PassiveMob
        assertEquals(Position(2, 2), passive.position)

        mapState.handleInput(StateProperties.moveDown)
        assertEquals(Position(0, 0), hero.position)
        assertEquals(Position(2, 0), aggressive.position)
        assertEquals(Position(1, 1), coward.position)
        assertEquals(Position(2, 2), passive.position)
    }

    @Test
    fun `test map mode attack`() {
        val hero = Hero(1000, 1000, 10, Position(0, 0), mutableListOf())
        val initField = GameField(listOf())
        val gameModel = GameModel(initField, mutableListOf(), hero)
        val mapView = mockk<MapView>(relaxed = true)
        val gameSound = mockk<GameSound>(relaxed = true)
        val itemFactory = ItemFactoryImpl()
        val gameFieldFactory = GameFieldFactoryImpl(5, 3, itemFactory)
        val gameProperties = mockk<GameProperties>(relaxed = true)
        every { gameProperties.levelsOrder } returns listOf("attack")
        every { gameProperties.confusionTime } returns 0
        val gameOverState = mockk<State>()
        val victoryState = mockk<State>()
        val mapState = MapState(
            gameModel,
            mapView,
            gameSound,
            mapOf(),
            gameFieldFactory,
            gameProperties,
            gameOverState,
            victoryState
        )

        mapState.activate()
        val aggressive = gameModel.mobs[0] as AggressiveMob
        val coward = gameModel.mobs[1] as CowardMob
        val passive = gameModel.mobs[2] as PassiveMob

        mapState.handleInput(StateProperties.moveLeft)
        assertEquals(hero.maximumHealth - coward.attackDamage, hero.health)
        assertEquals(coward.maximumHealth - hero.attackDamage, coward.health)
        assertEquals(2, gameModel.mobs.size)

        mapState.handleInput(StateProperties.moveDown)
        assertEquals(hero.maximumHealth - coward.attackDamage - passive.attackDamage, hero.health)
        assertEquals(passive.maximumHealth - hero.attackDamage, passive.health)
        assertEquals(1, gameModel.mobs.size)

        mapState.handleInput(StateProperties.moveRight)
        assertEquals(hero.maximumHealth - coward.attackDamage - passive.attackDamage - 2 * aggressive.attackDamage, hero.health)
        assertEquals(aggressive.maximumHealth - 2 * hero.attackDamage, aggressive.health)
    }

    @Test
    fun `test map mode confused`() {
        val hero = Hero(1000, 1000, 0, Position(1, 1), mutableListOf())
        val initField = GameField(listOf())
        val gameModel = GameModel(initField, mutableListOf(), hero)
        val mapView = mockk<MapView>(relaxed = true)
        val gameSound = mockk<GameSound>(relaxed = true)
        val itemFactory = ItemFactoryImpl()
        val gameFieldFactory = GameFieldFactoryImpl(3, 3, itemFactory)
        val gameProperties = mockk<GameProperties>(relaxed = true)
        every { gameProperties.levelsOrder } returns listOf("confuse")
        every { gameProperties.confusionTime } returns 2
        val gameOverState = mockk<State>()
        val victoryState = mockk<State>()
        val mapState = MapState(
            gameModel,
            mapView,
            gameSound,
            mapOf(),
            gameFieldFactory,
            gameProperties,
            gameOverState,
            victoryState
        )

        mapState.activate()
        val passive = gameModel.mobs[0] as PassiveMob
        assertEquals(Position(1, 2), passive.position)

        mapState.handleInput(StateProperties.moveDown)
        val position1 = passive.position
        assertIs<RandomMobDecorator>(gameModel.mobs[0])

        mapState.handleInput(StateProperties.moveUp)
        val position2 = passive.position
        assertIs<PassiveMob>(gameModel.mobs[0])

        assertTrue(position1 != Position(1, 2) || position2 != Position(1, 2))
    }
}
