package com.marklodev.modtesting.registries

import com.marklodev.modtesting.adapters.BlockStateAdapter
import com.marklodev.modtesting.adapters.toAdapter
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier

interface BlockRegistry {
    fun getDefaultBlockState(id: Identifier): BlockStateAdapter
}

object MinecraftBlockRegistry: BlockRegistry {
    private val registry = Registries.BLOCK

    override fun getDefaultBlockState(id: Identifier) = registry.get(id).defaultState.toAdapter()
}