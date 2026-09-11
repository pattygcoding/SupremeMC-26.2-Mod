package com.suprememc.content.init;

import com.mojang.serialization.MapCodec;
import com.suprememc.Constants;
import com.suprememc.content.worldgen.HybridEndBiomeSource;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.biome.BiomeSource;

public final class ModBiomeSources {
    public static final MapCodec<HybridEndBiomeSource> HYBRID_END = Registry.register(
        BuiltInRegistries.BIOME_SOURCE,
        Identifier.fromNamespaceAndPath(Constants.MOD_ID, "hybrid_end"),
        HybridEndBiomeSource.CODEC);

    private ModBiomeSources() {
    }

    public static void bootstrap() {
    }
}
