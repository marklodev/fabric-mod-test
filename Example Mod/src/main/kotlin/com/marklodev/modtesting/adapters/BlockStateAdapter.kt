package com.marklodev.modtesting.adapters

import net.minecraft.block.BlockState

interface BlockStateAdapter {
    fun isAir(): Boolean

    fun getMinecraftBlockState(): BlockState? = null
}

fun BlockState.toAdapter(): BlockStateAdapter = object : BlockStateAdapter {
    override fun isAir() = this@toAdapter.isAir

    override fun getMinecraftBlockState() = this@toAdapter
}