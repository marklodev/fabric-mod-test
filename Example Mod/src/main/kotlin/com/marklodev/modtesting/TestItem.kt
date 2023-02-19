package com.marklodev.modtesting

import net.minecraft.entity.player.PlayerEntity as MinecraftPlayerEntity
import net.minecraft.block.BlockState as MinecraftBlockState
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World as MinecraftWorld

class TestItem(settings: Settings): Item(settings) {

    private val delegate = TestItemBehavior()

    override fun use(world: MinecraftWorld?, user: MinecraftPlayerEntity?, hand: Hand?): TypedActionResult<ItemStack> {
        val stack = ItemStack(this)
        val result = delegate.use(user?.toPlayerEntity(), world?.toWorld())
        return TypedActionResult(result, stack)
    }

}

interface BlockState {

    fun isAir(): Boolean

}

fun MinecraftBlockState.toBlockState(): BlockState = object : BlockState {

    override fun isAir() = this@toBlockState.isAir
}

interface World {
    fun getBlockState(pos: BlockPos): BlockState?
}

fun MinecraftWorld.toWorld(): World = object : World {
    override fun getBlockState(pos: BlockPos) = this@toWorld.getBlockState(pos)?.toBlockState()
}

interface PlayerEntity {

    fun getPos(): Vec3d
    fun setPos(pos: Vec3d)

}

fun MinecraftPlayerEntity.toPlayerEntity(): PlayerEntity = object : PlayerEntity {

    override fun getPos(): Vec3d = this@toPlayerEntity.pos
    override fun setPos(pos: Vec3d): Unit = this@toPlayerEntity.setPosition(pos)

}

class TestItemBehavior {

    private val teleportDistance = 4

    fun use(user: PlayerEntity?, world: World?): ActionResult {
        user?.let {
            val pos = it.getPos()
            val newPos = findTeleportDestination(pos, world)
            it.setPos(newPos)
        }
        return ActionResult.SUCCESS
    }

    private fun findTeleportDestination(playerPos: Vec3d, world: World?): Vec3d {
        if (world == null) {
            return playerPos
        }
        for (i in teleportDistance downTo 0) {
            val destination = playerPos.add(-i.toDouble(), 0.0, 0.0)
            if (world.getBlockState(BlockPos(destination))?.isAir() != false) {
                return destination
            }
        }
        return playerPos
    }

}