package com.suprememc.content.blocks;

import com.suprememc.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NyliumBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public final class LavenderEndsparBlock extends NyliumBlock {
    private static final ResourceKey<ConfiguredFeature<?, ?>> VEGETATION_FEATURE = ResourceKey.create(
        Registries.CONFIGURED_FEATURE,
        Identifier.fromNamespaceAndPath(Constants.MOD_ID, "lavender_endspar_vegetation_bonemeal")
    );

    public LavenderEndsparBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockPos above = pos.above();
        BlockState aboveState = level.getBlockState(above);
        if (aboveState.getLightDampening() >= 15) {
            level.setBlockAndUpdate(pos, Blocks.END_STONE.defaultBlockState());
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        BlockPos above = pos.above();
        return level.getBlockState(above).isAir() && level.isInsideBuildHeight(above);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        growVegetation(level, random, pos);
    }

    public static void growVegetation(ServerLevel level, RandomSource random, BlockPos pos) {
        Registry<ConfiguredFeature<?, ?>> features = level.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE);
        ChunkGenerator generator = level.getChunkSource().getGenerator();
        features.get(VEGETATION_FEATURE).ifPresent(feature -> feature.value().place(level, generator, random, pos.above()));
    }
}