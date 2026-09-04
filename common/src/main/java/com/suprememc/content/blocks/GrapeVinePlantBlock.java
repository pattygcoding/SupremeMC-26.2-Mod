package com.suprememc.content.blocks;

import com.mojang.serialization.MapCodec;
import com.suprememc.content.ModContent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/** The body/stem segment of a {@link GrapeVineBlock}, mirroring vanilla {@code weeping_vines_plant}. */
public final class GrapeVinePlantBlock extends GrowingPlantBodyBlock {
    public static final MapCodec<GrapeVinePlantBlock> CODEC = simpleCodec(GrapeVinePlantBlock::new);

    public GrapeVinePlantBlock(BlockBehaviour.Properties properties) {
        super(properties, Direction.DOWN, Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D), false);
    }

    @Override
    public MapCodec<GrapeVinePlantBlock> codec() {
        return CODEC;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return GrapeVineBlock.isSupport(level.getBlockState(pos.above()));
    }

    @Override
    protected GrowingPlantHeadBlock getHeadBlock() {
        return (GrowingPlantHeadBlock) ModContent.GRAPE_VINE;
    }

    @Override
    protected Block getBodyBlock() {
        return this;
    }
}
