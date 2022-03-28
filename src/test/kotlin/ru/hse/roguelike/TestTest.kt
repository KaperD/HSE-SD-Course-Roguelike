package ru.hse.roguelike

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import ru.hse.roguelike.property.StringProperties

class TestTest {
    @Test
    fun test() {
        Assertions.assertEquals("""
            Open Map        = m
            Map Free Mode   = f
            Open Inventory  = i
            Open Help       = h
            Move Up         = Arrow Up
            Move Down       = Arrow Down
            Move Left       = Arrow Left
            Move Right      = Arrow Right
            Use/Cancel Item = Enter
            Exit Game       = esc
        """.trimIndent(), StringProperties.helpMessage)
    }
}
