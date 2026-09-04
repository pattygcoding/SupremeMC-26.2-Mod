package com.suprememc.content.blocks;

import com.mojang.serialization.MapCodec;
import com.suprememc.content.ModContent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrowingPlantBodyBlock;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public final class CornStalkPlantBlock extends GrowingPlantBodyBlock {
    public static final MapCodec<CornStalkPlantBlock> CODEC = simpleCodec(CornStalkPlantBlock::new);

    public CornStalkPlantBlock(BlockBehaviour.Properties properties) {
        super(properties, Direction.UP, Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D), false);
    }

    @Override
    public MapCodec<CornStalkPlantBlock> codec() {
        return CODEC;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        return below.is(ModContent.CORN_STALK) || below.is(this)
            || below.is(Blocks.GRASS_BLOCK) || below.is(BlockTags.DIRT);
    }

    @Override
    protected GrowingPlantHeadBlock getHeadBlock() {
        return (GrowingPlantHeadBlock) ModContent.CORN_STALK;
    }

    @Override
    protected Block getBodyBlock() {
        return this;
    }
}