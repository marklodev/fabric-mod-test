package com.marklodev.modtesting

import com.marklodev.modtesting.adapters.*
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class TeleporterTest {

    @Test
    fun `Teleports 4 blocks away`() {
        val item = TeleporterBehavior()

        val user = PlayerDouble(10.0, 0.0, 0.0)
        val world = WorldDouble()
        world.addBlock(6, 0, 0)
        world.addBlock(7, 0, 0)

        item.use(user, world)

        Assertions.assertThat(user)
            .extracting({it.x}, {it.y}, {it.z})
            .containsExactly(8.0, 0.0, 0.0)
    }
}

