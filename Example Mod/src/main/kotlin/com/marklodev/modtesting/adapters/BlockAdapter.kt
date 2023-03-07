package com.marklodev.modtesting.adapters

import net.minecraft.block.Block

interface BlockAdapter {

    fun getMinecraftBlock(): Block? = null
}

fun Block.toAdapter(): BlockAdapter = object : BlockAdapter {
    override fun getMinecraftBlock() = this@toAdapter
    override fun equals(other: Any?) = when (other) {
        is BlockAdapter -> this@toAdapter == other.getMinecraftBlock()
        else -> false
    }

    override fun hashCode(): Int = this@toAdapter.hashCode()
}