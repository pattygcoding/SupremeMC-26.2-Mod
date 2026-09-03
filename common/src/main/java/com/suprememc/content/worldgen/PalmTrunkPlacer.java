package com.suprememc.content.worldgen;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.suprememc.content.ModContent;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.List;
import java.util.function.BiConsumer;

// Leans the trunk a fixed random direction as it rises, instead of a perfectly vertical column, so it reads as a palm.
public class PalmTrunkPlacer extends TrunkPlacer {
    public static final MapCodec<PalmTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(instance ->
        palmTrunkParts(instance).apply(instance, PalmTrunkPlacer::new));

    // 8 compass directions the trunk can lean toward.
    private static final int[][] DIRECTIONS = {
        {1, 0}, {-1, 0}, {0, 1}, {0, -1},
        {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
    };

    private final int bendStartHeight;
    private final IntProvider bendInterval;
    private final int maxLean;

    public PalmTrunkPlacer(int baseHeight, int heightRandA, int heightRandB, int bendStartHeight, IntProvider bendInterval, int maxLean) {
        super(baseHeight, heightRandA, heightRandB);
        this.bendStartHeight = bendStartHeight;
        this.bendInterval = bendInterval;
        this.maxLean = maxLean;
    }

    private static <P extends PalmTrunkPlacer> Products.P6<RecordCodecBuilder.Mu<P>, Integer, Integer, Integer, Integer, IntProvider, Integer> palmTrunkParts(RecordCodecBuilder.Instance<P> instance) {
        return trunkPlacerParts(instance)
            .and(Codec.intRange(0, 32).fieldOf("bend_start_height").forGetter(PalmTrunkPlacer::getBendStartHeight))
            .and(IntProviders.codec(1, 8).fieldOf("bend_interval").forGetter(PalmTrunkPlacer::getBendInterval))
            .and(Codec.intRange(0, 16).fieldOf("max_lean").forGetter(PalmTrunkPlacer::getMaxLean));
    }

    private int getBendStartHeight() {
        return this.bendStartHeight;
    }

    private IntProvider getBendInterval() {
        return this.bendInterval;
    }

    private int getMaxLean() {
        return this.maxLean;
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return ModContent.PALM_TRUNK_PLACER_TYPE;
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(WorldGenLevel level, BiConsumer<BlockPos, BlockState> blockSetter, RandomSource random,
                                                              int freeTreeHeight, BlockPos pos, TreeConfiguration config) {
        placeBelowTrunkBlock(level, blockSetter, random, pos.below(), config);

        int[] direction = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
        int interval = Math.max(1, this.bendInterval.sample(random));
        BlockPos.MutableBlockPos cursor = pos.mutable();
        int leanApplied = 0;
        for (int y = 0; y < freeTreeHeight; y++) {
            placeLog(level, blockSetter, random, cursor, config);
            boolean shouldLean = y >= this.bendStartHeight && leanApplied < this.maxLean
                && (y - this.bendStartHeight) % interval == interval - 1;
            if (shouldLean) {
                cursor.move(direction[0], 1, direction[1]);
                leanApplied++;
            } else {
                cursor.move(0, 1, 0);
            }
        }
        return List.of(new FoliagePlacer.FoliageAttachment(cursor.immutable(), 0, false));
    }
}
