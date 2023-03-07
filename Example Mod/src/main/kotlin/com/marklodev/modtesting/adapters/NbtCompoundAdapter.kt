package com.marklodev.modtesting.adapters

import net.minecraft.block.entity.BlockEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.BlockPos

interface NbtCompoundAdapter {

    fun createBlockEntity(pos: BlockPos, state: BlockStateAdapter): BlockEntityAdapter?

}

fun NbtCompound.toAdapter() : NbtCompoundAdapter = object : NbtCompoundAdapter {
    override fun createBlockEntity(pos: BlockPos, state: BlockStateAdapter): BlockEntityAdapter? =
        BlockEntity.createFromNbt(pos, state.getMinecraftBlockState(), this@toAdapter)?.toAdapter()
}