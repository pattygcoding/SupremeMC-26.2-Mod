package com.suprememc.content.worldgen;

import com.mojang.serialization.Codec;
import com.suprememc.content.ModContent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * Hangs a chain of grape vines (2-6 blocks long) from the underside of a nearby leaves block,
 * mirroring how a player-planted grape vine grows but placed directly during worldgen.
 */
public class GrapeVineHangFeature extends Feature<NoneFeatureConfiguration> {
    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 6;
    private static final int SEARCH_RANGE = 6;

    public GrapeVineHangFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos.MutableBlockPos pos = context.origin().mutable();

        for (int i = 0; i < SEARCH_RANGE; i++, pos.move(Direction.DOWN)) {
            if (!level.getBlockState(pos).is(BlockTags.LEAVES)) {
                continue;
            }

            int length = MIN_LENGTH + random.nextInt(MAX_LENGTH - MIN_LENGTH + 1);
            BlockPos.MutableBlockPos scan = pos.mutable();
            int available = 0;
            for (int seg = 0; seg < length; seg++) {
                scan.move(Direction.DOWN);
                if (!level.getBlockState(scan).isAir()) {
                    break;
                }
                available++;
            }
            if (available < MIN_LENGTH) {
                return false;
            }

            BlockPos.MutableBlockPos vinePos = pos.mutable();
            for (int seg = 1; seg < available; seg++) {
                vinePos.move(Direction.DOWN);
                level.setBlock(vinePos, ModContent.GRAPE_VINE_PLANT.defaultBlockState(), Block.UPDATE_CLIENTS);
            }
            vinePos.move(Direction.DOWN);
            level.setBlock(vinePos, ModContent.GRAPE_VINE.defaultBlockState(), Block.UPDATE_CLIENTS);
            return true;
        }
        return false;
    }
}
