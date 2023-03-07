package com.marklodev.modtesting.adapters

import com.marklodev.modtesting.registries.BlockRegistry
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import java.util.UUID

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

data class BlockDouble(private val id: Identifier): BlockAdapter

data class BlockStateDouble(
    val x: Int,
    val y: Int,
    val z: Int,
    val id: Identifier = Identifier("minecraft", "air")
) : BlockStateAdapter {
    override val block = BlockDouble(id)
    override fun isAir() = (id == Identifier("minecraft", "air"))
}

data class BlockEntityDouble(
    override val pos: BlockPos,
    val id: String = UUID.randomUUID().toString()
) : BlockEntityAdapter {
    override fun createNbtWithId() = NbtCompoundDouble(id)
}
class NbtCompoundDouble(private val id: String) : NbtCompoundAdapter {
    override fun createBlockEntity(pos: BlockPos, state: BlockStateAdapter) = BlockEntityDouble(pos, id)
}

class WorldDouble(private val depth: Int = 10) : WorldAdapter {
    private val blockMap: MutableMap<BlockPos, BlockStateDouble> = mutableMapOf()
    private val blockEntityMap: MutableMap<BlockPos, BlockEntityAdapter> = mutableMapOf()
    val blocks = blockMap.values
    val blockEntities = blockEntityMap.values

    override fun getBlockState(pos: BlockPos): BlockStateAdapter {
        return if (pos.y <= -depth) {
            BlockStateDouble(pos.x, pos.y, pos.z, Identifier("minecraft", "bedrock"))
        } else {
            blockMap.getOrDefault(pos, BlockStateDouble(pos.x, pos.y, pos.z))
        }
    }

    override fun setBlockState(pos: BlockPos, blockState: BlockStateAdapter): Boolean {
        if (blockState.isAir()) {
            blockMap.remove(pos)
        } else {
            val double = blockState as BlockStateDouble
            blockMap[pos] = double.copy(x = pos.x, y = pos.y, z = pos.z)
        }
        return true
    }

    override fun getBlockEntity(pos: BlockPos) = blockEntityMap[pos]

    override fun removeBlockEntity(pos: BlockPos) {
        blockEntityMap.remove(pos)
    }

    override fun addBlockEntity(entity: BlockEntityAdapter) {
        blockEntityMap[entity.pos] = entity
    }

    fun addBlock(x: Int, y: Int, z: Int, id: Identifier = Identifier("minecraft", "dirt")) {
        val pos = BlockPos(x, y, z)
        blockMap[pos] = BlockStateDouble(x, y, z, id)
    }
}

class BlockRegistryDouble : BlockRegistry {
    override fun getDefaultBlockState(id: Identifier) = BlockStateDouble(0, 0, 0, id)

    override fun getBlock(id: Identifier) = BlockDouble(id)

}