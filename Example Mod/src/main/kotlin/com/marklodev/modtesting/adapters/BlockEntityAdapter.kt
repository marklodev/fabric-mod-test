package com.marklodev.modtesting.adapters

import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos

interface BlockEntityAdapter {
    val pos: BlockPos
    fun createNbtWithId(): NbtCompoundAdapter
    fun toMinecraftBlockEntity(): BlockEntity? = null
}

fun BlockEntity.toAdapter() : BlockEntityAdapter = object : BlockEntityAdapter {
    override val pos = this@toAdapter.pos
    override fun createNbtWithId() = this@toAdapter.createNbtWithId().toAdapter()
    override fun toMinecraftBlockEntity(): BlockEntity = this@toAdapter
}