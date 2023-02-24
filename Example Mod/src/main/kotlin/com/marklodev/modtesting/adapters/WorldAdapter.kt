package com.marklodev.modtesting.adapters

import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

interface WorldAdapter {
    fun getBlockState(pos: BlockPos): BlockStateAdapter
}

fun World.toAdapter(): WorldAdapter = object : WorldAdapter {
    override fun getBlockState(pos: BlockPos) = this@toAdapter.getBlockState(pos).toAdapter()
}