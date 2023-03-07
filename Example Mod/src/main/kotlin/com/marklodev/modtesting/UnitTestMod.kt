package com.marklodev.modtesting
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

@Suppress("UNUSED")
object UnitTestMod: ModInitializer {
    private const val MOD_ID = "unittestmod"
    override fun onInitialize() {
        println("Example mod has been initialized.")
        Registry.register(
            Registries.ITEM,
            Identifier("marklodev", "teleporter"),
            Teleporter(FabricItemSettings())
        )

        Registry.register(
            Registries.ITEM,
            Identifier("marklodev", "ore_attractor"),
            OreAttractor(FabricItemSettings())
        )
    }
}

