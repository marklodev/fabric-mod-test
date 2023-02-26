package com.marklodev.modtesting.adapters

import com.marklodev.modtesting.registries.BlockRegistry
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d

class PlayerDouble(var x: Double, var y: Double, var z: Double) : PlayerEntityAdapter {
    override fun getPos(): Vec3d {
        return Vec3d(x, y, z)
    }

    override fun setPos(pos: Vec3d) {
        x = pos.x
        y = pos.y
        z - pos.z
    }

    override fun getHorizontalFacing() = Direction.WEST

}

data class BlockStateDouble(
    val x: Int,
    val y: Int,
    val z: Int,
    private val isAir: Boolean = true,
    val id: Identifier = Identifier("minecraft", "air")
) : BlockStateAdapter {
    override fun isAir() = isAir
}

class WorldDouble : WorldAdapter {
    val blocks: MutableList<BlockStateDouble> = mutableListOf()

    override fun getBlockState(pos: BlockPos): BlockStateAdapter {
        return blocks.find { it.x == pos.x && it.y == pos.y && it.z == pos.z } ?: BlockStateDouble(pos.x, pos.y, pos.z)
    }

    override fun setBlockState(pos: BlockPos, blockState: BlockStateAdapter): Boolean {
        val double = blockState as BlockStateDouble
        blocks.add(
            double.copy(x = pos.x, y = pos.y, z = pos.z)
        )
        return true
    }

    fun addBlock(x: Int, y: Int, z: Int) {
        blocks.add(
            BlockStateDouble(x, y, z, false)
        )
    }
}

class BlockRegistryDouble : BlockRegistry {
    override fun getDefaultBlockState(id: Identifier) = BlockStateDouble(0, 0, 0, false, id)

}