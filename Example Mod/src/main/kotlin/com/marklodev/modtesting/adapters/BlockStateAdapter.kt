package com.marklodev.modtesting.adapters

import net.minecraft.block.BlockState

interface BlockStateAdapter {
    fun isAir(): Boolean
}

fun BlockState.toAdapter(): BlockStateAdapter = object : BlockStateAdapter {
    override fun isAir() = this@toAdapter.isAir
}