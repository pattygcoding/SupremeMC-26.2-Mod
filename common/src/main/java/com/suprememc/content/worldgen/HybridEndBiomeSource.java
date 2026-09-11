package com.suprememc.content.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;

import java.util.stream.Stream;

// Reproduces vanilla TheEndBiomeSource's exact chunk-distance check (see decompiled
// net.minecraft.world.level.biome.TheEndBiomeSource) to keep a strictly-vanilla center island and
// void gap around (0, 0), while delegating everything outside that radius to an arbitrary nested
// biome source (e.g. a multi_noise source mixing vanilla end biomes with custom ones).
public class HybridEndBiomeSource extends BiomeSource {
    public static final MapCodec<HybridEndBiomeSource> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            RegistryOps.retrieveElement(Biomes.THE_END),
            BiomeSource.CODEC.fieldOf("outer_biome_source").forGetter(source -> source.outer),
            Codec.INT.optionalFieldOf("radius_blocks", 1024).forGetter(source -> source.radiusBlocks))
        .apply(instance, HybridEndBiomeSource::new));

    private final Holder<Biome> end;
    private final BiomeSource outer;
    private final int radiusBlocks;
    private final long radiusChunksSquared;

    private HybridEndBiomeSource(Holder<Biome> end, BiomeSource outer, int radiusBlocks) {
        this.end = end;
        this.outer = outer;
        this.radiusBlocks = radiusBlocks;
        long radiusChunks = Math.max(1, radiusBlocks / 16);
        this.radiusChunksSquared = radiusChunks * radiusChunks;
    }

    @Override
    protected Stream<Holder<Biome>> collectPossibleBiomes() {
        return Stream.concat(Stream.of(this.end), this.outer.possibleBiomes().stream());
    }

    @Override
    protected MapCodec<? extends BiomeSource> codec() {
        return CODEC;
    }

    @Override
    public Holder<Biome> getNoiseBiome(int quartX, int quartY, int quartZ, Climate.Sampler sampler) {
        int blockX = QuartPos.toBlock(quartX);
        int blockZ = QuartPos.toBlock(quartZ);
        long chunkX = SectionPos.blockToSectionCoord(blockX);
        long chunkZ = SectionPos.blockToSectionCoord(blockZ);
        if (chunkX * chunkX + chunkZ * chunkZ <= this.radiusChunksSquared) {
            return this.end;
        }
        return this.outer.getNoiseBiome(quartX, quartY, quartZ, sampler);
    }
}
