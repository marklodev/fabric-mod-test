package com.marklodev.modtesting

import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class SmokeTest {

    @Test
    fun `Smoke test`() {
        val item = TestItemBehavior()
        val player = PlayerDouble(10.0, 2.0, 3.0)

        val world = WorldDouble()
        world.addBlock(BlockStateDouble(10, 2, 3, true))
        world.addBlock(BlockStateDouble(9, 2, 3, true))
        world.addBlock(BlockStateDouble(8, 2, 3, true))
        world.addBlock(BlockStateDouble(7, 2, 3, false))
        world.addBlock(BlockStateDouble(6, 2, 3, false))

        item.use(player, world)

        Assertions.assertThat(player)
            .extracting(
                {it.x}, {it.y}, {it.z}
            )
            .containsExactly(8.0, 2.0, 3.0)
    }
}

class PlayerDouble(var x: Double, var y: Double, var z: Double): PlayerEntity {

    override fun getPos() = Vec3d(x, y, z)

    override fun setPos(pos: Vec3d) {
        x = pos.x
        y = pos.y
        z = pos.z
    }

}

class BlockStateDouble(val x: Int, val y: Int, val z: Int, private val isAir: Boolean): BlockState {
    override fun isAir() = isAir
}

class WorldDouble: World {

    private val blocks: MutableList<BlockStateDouble> = mutableListOf()

    fun addBlock(block: BlockStateDouble) {
        blocks.add(block)
    }
    override fun getBlockState(pos: BlockPos): BlockState? {
        return blocks.find { it -> it.x == pos.x && it.y == pos.y && it.z == pos.z }
    }
}