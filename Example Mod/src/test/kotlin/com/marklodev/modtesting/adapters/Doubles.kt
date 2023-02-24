package com.marklodev.modtesting.adapters

import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d

class PlayerDouble(var x: Double, var y: Double, var z: Double): PlayerEntityAdapter {
    override fun getPos(): Vec3d {
        return Vec3d(x, y, z)
    }

    override fun setPos(pos: Vec3d) {
        x = pos.x
        y = pos.y
        z - pos.z
    }

}

class BlockStateDouble(val x: Int, val y: Int, val z: Int, private val isAir: Boolean = true): BlockStateAdapter {
    override fun isAir() = isAir
}

class WorldDouble: WorldAdapter {
    private val blocks: MutableList<BlockStateDouble> = mutableListOf()

    override fun getBlockState(pos: BlockPos): BlockStateAdapter {
        return blocks.find { it.x == pos.x && it.y == pos.y && it.z == pos.z } ?: BlockStateDouble(pos.x, pos.y, pos.z)
    }

    fun addBlock(x: Int, y: Int, z: Int) {
        blocks.add(
            BlockStateDouble(x, y, z, false)
        )
    }
}