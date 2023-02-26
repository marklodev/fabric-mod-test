package com.marklodev.modtesting

import com.marklodev.modtesting.adapters.PlayerEntityAdapter
import com.marklodev.modtesting.adapters.WorldAdapter
import com.marklodev.modtesting.adapters.toAdapter
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i
import net.minecraft.world.World

class Teleporter(settings: Settings): Item(settings) {

    private val behavior = TeleporterBehavior()

    override fun use(world: World?, user: PlayerEntity?, hand: Hand?): TypedActionResult<ItemStack> {
        val stack = ItemStack(this)
        val result = behavior.use(user?.toAdapter(), world?.toAdapter())
        return TypedActionResult(result, stack)
    }

}

class TeleporterBehavior {

    private val teleportDistance = 4

    fun use(user: PlayerEntityAdapter?, world: WorldAdapter?): ActionResult {
        user?.let {
            val pos = user.getPos()
            val direction = user.getHorizontalFacing()
            val newPos = findTeleportDestination(pos, direction.vector, world)
            user.setPos(newPos)
        }
        return ActionResult.SUCCESS
    }

    private fun findTeleportDestination(playerPos: Vec3d, direction: Vec3i, world: WorldAdapter?): Vec3d {
        if (world == null) {
            return playerPos
        }
        for (i in teleportDistance downTo 0) {
            val offset = direction.multiply(i)
            val destination = playerPos.add(offset.x.toDouble(), offset.y.toDouble(), offset.z.toDouble())
            if (world.getBlockState(BlockPos(destination)).isAir()) {
                return destination
            }
        }
        return playerPos
    }
}

