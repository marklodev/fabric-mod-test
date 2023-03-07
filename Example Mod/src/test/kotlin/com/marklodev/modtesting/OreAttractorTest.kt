package com.marklodev.modtesting

import com.marklodev.modtesting.adapters.*
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import org.assertj.core.api.Assertions.*
import org.assertj.core.groups.Tuple
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class OreAttractorTest {

    @ParameterizedTest
    @CsvSource("coal_ore", "iron_ore", "gold_ore", "diamond_ore")
    fun `pulls the ore up`(block: String) {
        val item = OreAttractorBehavior(BlockRegistryDouble())

        val user = PlayerDouble(0.0, 0.0, 0.0)
        val world = WorldDouble()
        world.addBlock(1, -2, 2, Identifier("minecraft", "dirt"))
        world.addBlock(1, -3, 2, Identifier("minecraft", block))

        item.use(user, world)

        assertThat(world.blocks)
            .extracting({it.x}, {it.y}, {it.z}, {it.block})
            .contains(
                Tuple.tuple(1, -2, 2, BlockDouble(Identifier("minecraft", block))),
                Tuple.tuple(1, -3, 2, BlockDouble(Identifier("minecraft", "dirt"))),
            )
    }

    @Test
    fun `Does not affect ores more than 4 blocks away`() {
        val item = OreAttractorBehavior(BlockRegistryDouble())

        val user = PlayerDouble(0.0, 0.0, 0.0)
        val world = WorldDouble()
        world.addBlock(5, -2, 0, Identifier("minecraft", "coal_ore"))

        item.use(user, world)

        assertThat(world.blocks)
            .extracting({it.x}, {it.y}, {it.z}, {it.block})
            .contains(
                Tuple.tuple(5, -2, 0, BlockDouble(Identifier("minecraft", "coal_ore"))),
            )
    }

    @Test
    fun `Pushes the block entity down`() {
        val item = OreAttractorBehavior(BlockRegistryDouble())

        val user = PlayerDouble(0.0, 0.0, 0.0)
        val world = WorldDouble()
        world.addBlock(1, -2, 1, Identifier("minecraft", "coal_ore"))
        world.addBlock(1, -1, 1, Identifier("minecraft", "chest"))
        world.addBlockEntity(BlockEntityDouble(BlockPos(1, -1, 1), "Content"))

        item.use(user, world)

        assertThat(world.blockEntities)
            .containsExactly(BlockEntityDouble(BlockPos(1, -2, 1), "Content"))
    }
}