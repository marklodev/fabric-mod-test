package com.marklodev.modtesting.adapters

import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

interface WorldAdapter {
    fun getBlockState(pos: BlockPos): BlockStateAdapter
    fun setBlockState(pos: BlockPos, blockState: BlockStateAdapter): Boolean
    fun getBlockEntity(pos: BlockPos): BlockEntityAdapter?
    fun removeBlockEntity(pos: BlockPos)
    fun addBlockEntity(entity: BlockEntityAdapter)
}

fun World.toAdapter(): WorldAdapter = object : WorldAdapter {
    override fun getBlockState(pos: BlockPos) = this@toAdapter.getBlockState(pos).toAdapter()

    override fun setBlockState(pos: BlockPos, blockState: BlockStateAdapter) =
        this@toAdapter.setBlockState(pos, blockState.getMinecraftBlockState())

    override fun getBlockEntity(pos: BlockPos) = this@toAdapter.getBlockEntity(pos)?.toAdapter()

    override fun removeBlockEntity(pos: BlockPos) = this@toAdapter.removeBlockEntity(pos)

    override fun addBlockEntity(entity: BlockEntityAdapter) =
        this@toAdapter.addBlockEntity(entity.toMinecraftBlockEntity())
}