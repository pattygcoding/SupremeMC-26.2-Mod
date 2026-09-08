package com.suprememc.content.worldgen;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.suprememc.content.init.ModFoliagePlacerTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

// Places leaves as radiating, drooping fronds around the trunk top instead of a round leaf blob, so trees read as palms.
public class PalmFoliagePlacer extends FoliagePlacer {
    public static final MapCodec<PalmFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec(instance ->
        palmParts(instance).apply(instance, PalmFoliagePlacer::new));

    // 8 compass directions a frond can radiate along from the trunk top.
    private static final int[][] DIRECTIONS = {
        {1, 0}, {-1, 0}, {0, 1}, {0, -1},
        {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
    };

    private final int frondCount;
    private final int frondLength;

    public PalmFoliagePlacer(IntProvider radius, IntProvider offset, int frondCount, int frondLength) {
        super(radius, offset);
        this.frondCount = frondCount;
        this.frondLength = frondLength;
    }

    private static <P extends PalmFoliagePlacer> Products.P4<RecordCodecBuilder.Mu<P>, IntProvider, IntProvider, Integer, Integer> palmParts(RecordCodecBuilder.Instance<P> instance) {
        return foliagePlacerParts(instance)
            .and(Codec.intRange(1, 8).fieldOf("frond_count").forGetter(PalmFoliagePlacer::getFrondCount))
            .and(Codec.intRange(1, 8).fieldOf("frond_length").forGetter(PalmFoliagePlacer::getFrondLength));
    }

    private int getFrondCount() {
        return this.frondCount;
    }

    private int getFrondLength() {
        return this.frondLength;
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return ModFoliagePlacerTypes.PALM;
    }

    @Override
    protected void createFoliage(WorldGenLevel level, FoliageSetter blockSetter, RandomSource random, TreeConfiguration config,
                                  int maxFreeTreeHeight, FoliageAttachment attachment, int foliageHeight, int radius, int offset) {
        BlockPos center = attachment.pos();
        placeCoreCluster(level, blockSetter, random, config, center);

        int count = Math.min(this.frondCount, DIRECTIONS.length);
        for (int i = 0; i < count; i++) {
            int[] direction = DIRECTIONS[i];
            int[] perpendicular = {-direction[1], direction[0]};
            int length = Math.max(3, this.frondLength - random.nextInt(2));
            for (int step = 1; step <= length; step++) {
                // Droop grows quadratically so each step lands at a different height, arching the frond
                // down toward its tip instead of stacking several steps into one flat plate.
                int droop = Math.round((float) (step * step) / length);
                BlockPos curvePos = center.offset(direction[0] * step, -droop, direction[1] * step);
                boolean thick = step > length / 2;
                placeFrondSegment(level, blockSetter, random, config, curvePos, perpendicular, thick);
            }
        }
    }

    // A round, tube-like cross-section along the frond's curve instead of a flat single-layer plate.
    private void placeFrondSegment(WorldGenLevel level, FoliageSetter blockSetter, RandomSource random, TreeConfiguration config,
                                    BlockPos curvePos, int[] perpendicular, boolean thick) {
        tryPlaceLeaf(level, blockSetter, random, config, curvePos);
        tryPlaceLeaf(level, blockSetter, random, config, curvePos.below());
        if (thick) {
            tryPlaceLeaf(level, blockSetter, random, config, curvePos.offset(perpendicular[0], 0, perpendicular[1]));
            tryPlaceLeaf(level, blockSetter, random, config, curvePos.offset(-perpendicular[0], 0, -perpendicular[1]));
            tryPlaceLeaf(level, blockSetter, random, config, curvePos.above());
        }
    }

    // Fills the crown between frond bases with a solid plus-shaped cluster so the top isn't a bare trunk cap.
    private void placeCoreCluster(WorldGenLevel level, FoliageSetter blockSetter, RandomSource random, TreeConfiguration config, BlockPos center) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                if (dx == 0 || dz == 0) {
                    tryPlaceLeaf(level, blockSetter, random, config, center.offset(dx, 0, dz));
                    tryPlaceLeaf(level, blockSetter, random, config, center.offset(dx, -1, dz));
                }
            }
        }
    }

    @Override
    public int foliageHeight(RandomSource random, int height, TreeConfiguration config) {
        return 1;
    }

    @Override
    public int foliageRadius(RandomSource random, int radius) {
        return radius + this.frondLength;
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource random, int localX, int localY, int localZ, int range, boolean large) {
        return false;
    }
}
