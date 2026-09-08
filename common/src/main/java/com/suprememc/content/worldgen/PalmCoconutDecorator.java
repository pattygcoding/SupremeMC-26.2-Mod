package com.suprememc.content.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.suprememc.content.ModContent;
import com.suprememc.content.init.ModTreeDecorators;
import com.suprememc.content.blocks.CoconutBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.List;

// Attaches coconuts of random ripeness to the palm trunk during tree generation, mirroring vanilla's
// CocoaDecorator on jungle trees - except pods hang just below the canopy instead of near the trunk base.
public class PalmCoconutDecorator extends TreeDecorator {
    public static final MapCodec<PalmCoconutDecorator> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(PalmCoconutDecorator::getProbability))
        .apply(instance, PalmCoconutDecorator::new));

    private final float probability;

    public PalmCoconutDecorator(float probability) { this.probability = probability; }

    public float getProbability() { return this.probability; }

    @Override
    protected TreeDecoratorType<?> type() { return ModTreeDecorators.PALM_COCONUT; }

    @Override
    public void place(TreeDecorator.Context context) {
        RandomSource random = context.random();
        if (random.nextFloat() >= this.probability) return;
        List<BlockPos> logs = context.logs();
        int topY = logs.stream().mapToInt(BlockPos::getY).max().orElse(0);
        logs.stream().filter(pos -> topY - pos.getY() <= 2).forEach(pos -> {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                if (random.nextFloat() <= 0.25F) {
                    BlockPos podPos = pos.relative(direction);
                    if (context.isAir(podPos)) {
                        // CoconutBlock's FACING points toward the supporting log, so face back at the trunk.
                        context.setBlock(podPos, ModContent.COCONUT.defaultBlockState()
                            .setValue(CoconutBlock.AGE, random.nextInt(CoconutBlock.MAX_AGE + 1))
                            .setValue(CoconutBlock.FACING, direction.getOpposite()));
                    }
                }
            }
        });
    }
}
