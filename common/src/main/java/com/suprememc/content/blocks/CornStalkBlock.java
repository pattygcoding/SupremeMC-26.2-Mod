package com.suprememc.content.blocks;

import com.mojang.serialization.MapCodec;
import com.suprememc.content.ModContent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public final class CornStalkBlock extends GrowingPlantHeadBlock {
    public static final MapCodec<CornStalkBlock> CODEC = simpleCodec(CornStalkBlock::new);
    private static final int MAX_HEIGHT = 3;

    public CornStalkBlock(BlockBehaviour.Properties properties) {
        super(properties, Direction.UP, Block.box(1.0D, 0.0D, 1.0D, 15.0D, 16.0D, 15.0D), false, 0.1D);
    }

    @Override
    public MapCodec<CornStalkBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return countSegmentsBelow(context.getLevel(), context.getClickedPos()) >= MAX_HEIGHT
            ? null
            : super.getStateForPlacement(context);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        return below.is(this) || below.is(ModContent.CORN_STALK_PLANT)
            || below.is(Blocks.GRASS_BLOCK) || below.is(BlockTags.DIRT);
    }

    @Override
    protected Block getBodyBlock() {
        return ModContent.CORN_STALK_PLANT;
    }

    @Override
    protected int getBlocksToGrowWhenBonemealed(RandomSource random) {
        double chance = 0.826D;
        int blocks = 1;
        while (random.nextDouble() < chance) {
            blocks++;
            chance *= 0.826D;
        }
        return blocks;
    }

    @Override
    protected boolean canGrowInto(BlockState state) {
        return state.isAir();
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (countSegmentsBelow(level, pos) < MAX_HEIGHT) {
            super.randomTick(state, level, pos, random);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return countSegmentsBelow(level, pos) < MAX_HEIGHT && super.isValidBonemealTarget(level, pos, state);
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        BlockPos nextPos = pos.above();
        int age = Math.min(state.getValue(AGE) + 1, MAX_AGE);
        int growth = Math.min(getBlocksToGrowWhenBonemealed(random), MAX_HEIGHT - countSegmentsBelow(level, pos));
        for (int grown = 0; grown < growth && canGrowInto(level.getBlockState(nextPos)) && !level.isOutsideBuildHeight(nextPos); grown++) {
            level.setBlockAndUpdate(nextPos, state.setValue(AGE, age));
            nextPos = nextPos.above();
        }
    }

    static int countSegmentsBelow(LevelReader level, BlockPos pos) {
        int height = 0;
        BlockPos currentPos = pos;
        while (level.getBlockState(currentPos).is(ModContent.CORN_STALK)
            || level.getBlockState(currentPos).is(ModContent.CORN_STALK_PLANT)) {
            height++;
            currentPos = currentPos.below();
        }
        return height;
    }
}