package com.suprememc.content.blocks;

import com.mojang.serialization.MapCodec;
import com.suprememc.content.ModContent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Grape vines hang and grow downward the same way {@code minecraft:weeping_vines} do, except growth
 * is capped at {@link #MAX_LENGTH} segments and they can only take root under leaves or moss.
 */
public final class GrapeVineBlock extends GrowingPlantHeadBlock {
    public static final MapCodec<GrapeVineBlock> CODEC = simpleCodec(GrapeVineBlock::new);
    private static final int MAX_LENGTH = 3;

    public GrapeVineBlock(BlockBehaviour.Properties properties) {
        super(properties, Direction.DOWN, Block.box(2.0D, 0.0D, 2.0D, 14.0D, 16.0D, 14.0D), false, 0.25D);
    }

    @Override
    public MapCodec<GrapeVineBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return countSegmentsAbove(context.getLevel(), context.getClickedPos()) >= MAX_LENGTH
            ? null
            : super.getStateForPlacement(context);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return isSupport(level.getBlockState(pos.above()));
    }

    @Override
    protected Block getBodyBlock() {
        return ModContent.GRAPE_VINE_PLANT;
    }

    @Override
    protected int getBlocksToGrowWhenBonemealed(RandomSource random) {
        return 1;
    }

    @Override
    protected boolean canGrowInto(BlockState state) {
        return state.isAir();
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (countSegmentsAbove(level, pos) < MAX_LENGTH) {
            super.randomTick(state, level, pos, random);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return countSegmentsAbove(level, pos) < MAX_LENGTH && super.isValidBonemealTarget(level, pos, state);
    }

    static boolean isSupport(BlockState state) {
        return state.is(BlockTags.LEAVES) || state.is(ModContent.PALM_LEAVES)
            || state.is(Blocks.MOSS_BLOCK) || state.is(Blocks.MOSS_CARPET)
            || state.is(Blocks.PALE_MOSS_BLOCK) || state.is(Blocks.PALE_MOSS_CARPET)
            || state.is(ModContent.GRAPE_VINE) || state.is(ModContent.GRAPE_VINE_PLANT);
    }

    static int countSegmentsAbove(LevelReader level, BlockPos pos) {
        int length = 0;
        BlockPos currentPos = pos;
        while (level.getBlockState(currentPos).is(ModContent.GRAPE_VINE)
            || level.getBlockState(currentPos).is(ModContent.GRAPE_VINE_PLANT)) {
            length++;
            currentPos = currentPos.above();
        }
        return length;
    }
}
