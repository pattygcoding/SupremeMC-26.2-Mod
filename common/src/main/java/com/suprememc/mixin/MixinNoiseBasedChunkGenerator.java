package com.suprememc.mixin;

import com.suprememc.Constants;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NoiseBasedChunkGenerator.class)
public abstract class MixinNoiseBasedChunkGenerator {
    private static final ResourceKey<Biome> CAYS =
        ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "cays"));
    // Matches the shallow topsoil depth vanilla surface rules give mangrove swamp/plains (1 top layer + a
    // few subsoil layers) for the re-skin: sand on top, sandstone underneath.
    private static final int SOIL_DEPTH = 4;
    // How high above sea level the flattened plateau sits, matching mangrove swamp's low, near-sea-level
    // land instead of the tall hills the reused mushroom-fields island noise naturally generates.
    private static final int FLAT_HEIGHT_ABOVE_SEA_LEVEL = 1;

    @Inject(method = "doFill", at = @At("RETURN"))
    private void suprememc$reskinCays(
        Blender blender,
        StructureManager structureManager,
        RandomState randomState,
        ChunkAccess chunk,
        int minCellY,
        int cellCountY,
        CallbackInfoReturnable<ChunkAccess> callback
    ) {
        int seaLevel = ((NoiseBasedChunkGenerator) (Object) this).getSeaLevel();
        int minBuildY = chunk.getHeightAccessorForGeneration().getMinY() + 1;
        Heightmap oceanFloor = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
        Heightmap worldSurface = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);
        int flatTop = seaLevel + FLAT_HEIGHT_ABOVE_SEA_LEVEL;

        suprememc$unifyCaysAndMushroomFieldsIslands(chunk, seaLevel);

        for (int localX = 0; localX < 16; localX++) {
            for (int localZ = 0; localZ < 16; localZ++) {
                if (!chunk.getNoiseBiome(localX >> 2, seaLevel >> 2, localZ >> 2).is(CAYS)) {
                    continue;
                }

                // Natural terrain top for this column (land hill or submerged ocean floor alike).
                int groundTop = chunk.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, localX, localZ);
                if (groundTop > flatTop) {
                    // Shave the hill down to the flat plateau; columns already at/below it (e.g. the
                    // natural coastline slope into the ocean) are left untouched so the island still
                    // tapers naturally into the sea instead of ending in a sheer cliff.
                    for (int y = groundTop; y > flatTop; y--) {
                        LevelChunkSection section = chunk.getSection(chunk.getSectionIndex(y));
                        if (section.getBlockState(localX & 15, y & 15, localZ & 15).isAir()) {
                            break;
                        }
                        setBlock(chunk, oceanFloor, worldSurface, localX, y, localZ, Blocks.AIR.defaultBlockState());
                    }
                    groundTop = flatTop;
                }
                int bottom = Math.max(minBuildY, groundTop - SOIL_DEPTH + 1);

                for (int y = groundTop; y >= bottom; y--) {
                    LevelChunkSection section = chunk.getSection(chunk.getSectionIndex(y));
                    if (section.getBlockState(localX & 15, y & 15, localZ & 15).isAir()) {
                        break;
                    }
                    setBlock(chunk, oceanFloor, worldSurface, localX, y, localZ, y == groundTop
                        ? Blocks.SAND.defaultBlockState()
                        : Blocks.SANDSTONE.defaultBlockState());
                }
            }
        }
    }

    // Cays shares its rare off-coast island climate slot with vanilla mushroom_fields (split by temperature,
    // see MixinOverworldBiomeBuilder), so a single island landmass can occasionally straddle the boundary
    // between the two. Whenever a chunk contains both, force its whole share of that island to Cays so one
    // landmass never renders as a patchwork of both, or as a flat-Cays/hilly-mushroom cliff down the middle.
    private static void suprememc$unifyCaysAndMushroomFieldsIslands(ChunkAccess chunk, int seaLevel) {
        int quartY = seaLevel >> 2;
        int caysCount = 0;
        int mushroomCount = 0;
        Holder<Biome> caysSample = null;
        for (int qx = 0; qx < 4; qx++) {
            for (int qz = 0; qz < 4; qz++) {
                Holder<Biome> biome = chunk.getNoiseBiome(qx, quartY, qz);
                if (biome.is(CAYS)) {
                    caysCount++;
                    caysSample = biome;
                } else if (biome.is(Biomes.MUSHROOM_FIELDS)) {
                    mushroomCount++;
                }
            }
        }
        if (caysCount == 0 || mushroomCount == 0) {
            return;
        }
        // Prefer Cays whenever a chunk straddles the split (rather than a population vote) so a lukewarm-ocean
        // cutoff through the middle of one landmass doesn't leave a mismatched flat-Cays/hilly-mushroom cliff
        // running through it; Cays simply claims the whole chunk's share of the island instead.
        Holder<Biome> winner = caysSample;
        for (int qx = 0; qx < 4; qx++) {
            for (int qz = 0; qz < 4; qz++) {
                Holder<Biome> biome = chunk.getNoiseBiome(qx, quartY, qz);
                if (biome.is(CAYS) || biome.is(Biomes.MUSHROOM_FIELDS)) {
                    setBiome(chunk, seaLevel, qx, qz, winner);
                }
            }
        }
    }

    // Mirrors setBlock below: LevelChunkSection's biome PalettedContainer also has a checked public path
    // (via ChunkAccess) that would deadlock inside doFill, but getAndSetUnchecked mutates it in place
    // safely, the same way vanilla's own fillBiomesFromNoise populates it.
    private static void setBiome(ChunkAccess chunk, int seaLevel, int qx, int qz, Holder<Biome> biome) {
        LevelChunkSection section = chunk.getSection(chunk.getSectionIndex(seaLevel));
        int qy = (seaLevel >> 2) & 3;
        @SuppressWarnings("unchecked")
        PalettedContainer<Holder<Biome>> biomes = (PalettedContainer<Holder<Biome>>) section.getBiomes();
        biomes.getAndSetUnchecked(qx, qy, qz, biome);
    }

    // fillFromNoise() already holds every section's (non-reentrant) threading-check semaphore for the
    // whole doFill() call (see its section.acquire()/release() around this method), so the normal
    // ChunkAccess.setBlockState(pos, state) -> ProtoChunk -> LevelChunkSection(checkThreading=true) path
    // deadlocks the calling worker thread forever trying to re-acquire its own lock. Vanilla's own doFill
    // avoids this the same way: write straight to the section with checkThreading=false and update the
    // heightmaps manually.
    private static void setBlock(ChunkAccess chunk, Heightmap oceanFloor, Heightmap worldSurface, int localX, int y, int localZ, BlockState state) {
        LevelChunkSection section = chunk.getSection(chunk.getSectionIndex(y));
        section.setBlockState(localX & 15, y & 15, localZ & 15, state, false);
        oceanFloor.update(localX, y, localZ, state);
        worldSurface.update(localX, y, localZ, state);
    }
}