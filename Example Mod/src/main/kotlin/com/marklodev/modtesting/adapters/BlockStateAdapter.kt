package com.marklodev.modtesting.adapters

import net.minecraft.block.BlockState

interface BlockStateAdapter {
    val block: BlockAdapter
    fun isAir(): Boolean

    fun getMinecraftBlockState(): BlockState? = null
}

fun BlockState.toAdapter(): BlockStateAdapter = object : BlockStateAdapter {
    override val block = this@toAdapter.block.toAdapter()
    override fun isAir() = this@toAdapter.isAir

    override fun getMinecraftBlockState() = this@toAdapter
}