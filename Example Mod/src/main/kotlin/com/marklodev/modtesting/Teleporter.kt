package com.marklodev.modtesting

import com.marklodev.modtesting.adapters.PlayerEntityAdapter
import com.marklodev.modtesting.adapters.WorldAdapter
import com.marklodev.modtesting.adapters.toAdapter
import com.marklodev.modtesting.registries.BlockRegistry
import com.marklodev.modtesting.registries.MinecraftBlockRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class Teleporter(settings: Settings): Item(settings) {

    private val behavior = TeleporterBehavior(MinecraftBlockRegistry)

    override fun use(world: World?, user: PlayerEntity?, hand: Hand?): TypedActionResult<ItemStack> {
        val stack = ItemStack(this)
        val result = behavior.use(user?.toAdapter(), world?.toAdapter())
        return TypedActionResult(result, stack)
    }

}

class TeleporterBehavior(private val blockRegistry: BlockRegistry) {

    private val teleportDistance = 4

    fun use(user: PlayerEntityAdapter?, world: WorldAdapter?): ActionResult {
        user?.let {
            teleport(user, world)
        }
        return ActionResult.SUCCESS
    }

    private fun teleport(player: PlayerEntityAdapter, world: WorldAdapter?) {
        if (world == null) {
            return
        }
        val netherrack = blockRegistry.getDefaultBlockState(Identifier("minecraft", "netherrack"))
        val fire = blockRegistry.getDefaultBlockState(Identifier("minecraft", "fire"))
        val playerPos = player.getPos()
        val direction = player.getHorizontalFacing().vector
        var teleported = false
        for (i in teleportDistance downTo 0) {
            val offset = direction.multiply(i)
            val destination = playerPos.add(offset.x.toDouble(), offset.y.toDouble(), offset.z.toDouble())
            if (!teleported) {
                if (world.getBlockState(BlockPos(destination)).isAir()) {
                    player.setPos(destination)
                    teleported = true
                }
            } else {
                world.setBlockState(
                    BlockPos(destination.add(0.0, -1.0, 0.0)),
                    netherrack
                )
                world.setBlockState(
                    BlockPos(destination),
                    fire
                )
            }
        }
    }
}

