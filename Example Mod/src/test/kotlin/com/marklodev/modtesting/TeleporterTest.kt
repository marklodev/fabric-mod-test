package com.marklodev.modtesting

import com.marklodev.modtesting.adapters.*
import net.minecraft.util.Identifier
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class TeleporterTest {

    @Test
    fun `Teleports 4 blocks away`() {
        val item = TeleporterBehavior(BlockRegistryDouble())

        val user = PlayerDouble(10.0, 0.0, 0.0)
        val world = WorldDouble()
        world.addBlock(6, 0, 0)
        world.addBlock(7, 0, 0)

        item.use(user, world)

        Assertions.assertThat(user)
            .extracting({it.x}, {it.y}, {it.z})
            .containsExactly(8.0, 0.0, 0.0)
    }

    @Test
    fun `Creates a netherrack path to the destination`() {
        val item = TeleporterBehavior(BlockRegistryDouble())

        val user = PlayerDouble(0.0, 0.0, 0.0)
        val world = WorldDouble()

        item.use(user, world)

        Assertions.assertThat(world.blocks)
            .contains(
                BlockStateDouble(0, -1, 0, Identifier("minecraft", "netherrack")),
                BlockStateDouble(-1, -1, 0, Identifier("minecraft", "netherrack")),
                BlockStateDouble(-2, -1, 0, Identifier("minecraft", "netherrack")),
                BlockStateDouble(-3, -1, 0, Identifier("minecraft", "netherrack"))
            )
    }

    @Test
    fun `Sets the path on fire`() {
        val item = TeleporterBehavior(BlockRegistryDouble())

        val user = PlayerDouble(0.0, 0.0, 0.0)
        val world = WorldDouble()

        item.use(user, world)

        Assertions.assertThat(world.blocks)
            .contains(
                BlockStateDouble(0, 0, 0, Identifier("minecraft", "fire")),
                BlockStateDouble(-1, 0, 0, Identifier("minecraft", "fire")),
                BlockStateDouble(-2, 0, 0, Identifier("minecraft", "fire")),
                BlockStateDouble(-3, 0, 0, Identifier("minecraft", "fire"))
            )
    }
}

