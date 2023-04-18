package com.marklodev.modtesting

import com.marklodev.modtesting.adapters.BlockStateAdapter
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
import kotlin.math.abs

class OreAttractor(settings: Settings) : Item(settings) {

    private val behavior = OreAttractorBehavior(MinecraftBlockRegistry)

    override fun use(world: World?, user: PlayerEntity?, hand: Hand?): TypedActionResult<ItemStack> {
        val stack = ItemStack(this)
        if (world == null || user == null) {
            return TypedActionResult.success(stack)
        }
        val result = behavior.use(user.toAdapter(), world.toAdapter())
        return TypedActionResult(result, stack)
    }
}

class OreAttractorBehavior(blockRegistry: BlockRegistry) {

    private val ores = setOf(
        blockRegistry.getBlock(Identifier("minecraft", "coal_ore")),
        blockRegistry.getBlock(Identifier("minecraft", "iron_ore")),
        blockRegistry.getBlock(Identifier("minecraft", "gold_ore")),
        blockRegistry.getBlock(Identifier("minecraft", "diamond_ore")),
    )
    private val bedrock = blockRegistry.getBlock(Identifier("minecraft", "bedrock"))

    fun use(player: PlayerEntityAdapter, world: WorldAdapter): ActionResult {
        val initialPos = BlockPos.ofFloored(player.getPos()).down(2)
        searchDownward(world, initialPos)?.let { result ->
            val (pos, blockState) = result
            val targetPos = pos.up()
            val swappedBlock = world.getBlockState(targetPos)

            val entity = world.getBlockEntity(targetPos)
            val savedNbt = entity?.createNbtWithId()
            savedNbt?.let { world.removeBlockEntity(targetPos) }

            world.setBlockState(targetPos, blockState)
            world.setBlockState(pos, swappedBlock)

            savedNbt?.let { nbt ->
                nbt.createBlockEntity(pos, swappedBlock)?. let { newEntity -> world.addBlockEntity(newEntity) }
            }
        }
        return ActionResult.SUCCESS
    }

    private fun isOre(blockState: BlockStateAdapter) = ores.contains(blockState.block)

    private fun searchDownward(world: WorldAdapter, initialPos: BlockPos): Pair<BlockPos, BlockStateAdapter>? {
        if (world.getBlockState(initialPos).block == bedrock) {
            return null
        }

        fun breadthSearch(pos: BlockPos): Pair<BlockPos, BlockStateAdapter>? {
            val posToSearch = mutableListOf(pos)
            val visited: MutableSet<BlockPos> = mutableSetOf()
            val maxRadius = 4

            while (posToSearch.isNotEmpty()) {
                val currentPos = posToSearch.removeFirst()
                if (!visited.add(currentPos)) {
                    continue
                }
                val radius = abs(currentPos.x - pos.x) + abs(currentPos.z - pos.z)
                if (radius > maxRadius) {
                    continue
                }

                val blockState = world.getBlockState(currentPos)
                if (isOre(blockState)) {
                    return Pair(currentPos, blockState)
                }

                posToSearch.add(currentPos.west())
                posToSearch.add(currentPos.north())
                posToSearch.add(currentPos.east())
                posToSearch.add(currentPos.south())
            }
            return null
        }

        return breadthSearch(initialPos) ?: searchDownward(world, initialPos.down())
    }
}