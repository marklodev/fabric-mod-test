package com.marklodev.modtesting.adapters

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d

interface PlayerEntityAdapter {

    fun getPos(): Vec3d
    fun setPos(pos: Vec3d)
    fun getHorizontalFacing(): Direction
}

fun PlayerEntity.toAdapter(): PlayerEntityAdapter = object: PlayerEntityAdapter {

    override fun getPos() = this@toAdapter.pos
    override fun setPos(pos: Vec3d) = this@toAdapter.setPosition(pos)
    override fun getHorizontalFacing() = this@toAdapter.horizontalFacing
}