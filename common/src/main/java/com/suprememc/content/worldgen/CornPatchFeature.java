package com.suprememc.content.worldgen;

import com.suprememc.content.ModContent;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public final class CornPatchFeature extends Feature<NoneFeatureConfiguration> {
    public CornPatchFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos basePos = context.origin();
        if (!level.getBlockState(basePos.below()).is(Blocks.GRASS_BLOCK)
            && !level.getBlockState(basePos.below()).is(BlockTags.DIRT)) {
            return false;
        }
        if (!level.getBlockState(basePos).isAir()
            || !level.getBlockState(basePos.above()).isAir()
            || !level.getBlockState(basePos.above(2)).isAir()) {
            return false;
        }

        setBlock(level, basePos, ModContent.CORN_STALK_PLANT.defaultBlockState());
        setBlock(level, basePos.above(), ModContent.CORN_STALK_PLANT.defaultBlockState());
        setBlock(level, basePos.above(2), ModContent.CORN_STALK.defaultBlockState());
        return true;
    }
}