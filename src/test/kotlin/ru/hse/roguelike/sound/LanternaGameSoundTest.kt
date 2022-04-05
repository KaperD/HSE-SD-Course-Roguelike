package ru.hse.roguelike.sound

import com.googlecode.lanterna.terminal.Terminal
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

internal class LanternaGameSoundTest {

    @Test
    fun `test beep`() {
        val terminal = mockk<Terminal>(relaxed = true)
        val sound = LanternaGameSound(terminal)
        sound.beep()
        verify { terminal.bell() }
        confirmVerified(terminal)
    }
}
