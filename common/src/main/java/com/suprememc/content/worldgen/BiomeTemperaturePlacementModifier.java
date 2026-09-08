package com.suprememc.content.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.suprememc.content.init.ModPlacementModifiers;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.stream.Stream;

// Filters positions by the base temperature of the biome column at the placement position, so beach
// palm density can be tiered by climate from data (hot/warm vs moderate) instead of hardcoded biome lists.
public class BiomeTemperaturePlacementModifier extends PlacementModifier {
    public static final MapCodec<BiomeTemperaturePlacementModifier> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            Codec.FLOAT.fieldOf("min_temperature").forGetter(BiomeTemperaturePlacementModifier::getMinTemperature),
            Codec.FLOAT.optionalFieldOf("max_temperature", Float.POSITIVE_INFINITY).forGetter(BiomeTemperaturePlacementModifier::getMaxTemperature))
        .apply(instance, BiomeTemperaturePlacementModifier::new));

    private final float minTemperature;
    private final float maxTemperature;

    public BiomeTemperaturePlacementModifier(float minTemperature, float maxTemperature) {
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
    }

    public float getMinTemperature() { return this.minTemperature; }

    public float getMaxTemperature() { return this.maxTemperature; }

    @Override
    public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
        // Placement runs after the heightmap modifier, so the biome sampled here is the surface biome
        // column and is not confused by an underground 3D (cave) biome.
        float temperature = context.getLevel().getBiome(pos).value().getBaseTemperature();
        return temperature >= this.minTemperature && temperature < this.maxTemperature ? Stream.of(pos) : Stream.empty();
    }

    @Override
    public PlacementModifierType<?> type() {
        return ModPlacementModifiers.BIOME_TEMPERATURE;
    }
}
