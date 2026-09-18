package com.suprememc.content.blocks;

import com.suprememc.content.ModContent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;

public final class SkylandsPortalShape {
    private SkylandsPortalShape() {
    }

    public static boolean tryCreate(LevelAccessor level, BlockPos pos) {
        return create(level, pos, Direction.Axis.X) || create(level, pos, Direction.Axis.Z);
    }

    public static boolean isComplete(BlockGetter level, BlockPos pos, Direction.Axis axis) {
        return find(level, pos, axis, false) != null;
    }

    private static boolean create(LevelAccessor level, BlockPos pos, Direction.Axis axis) {
        Shape shape = find(level, pos, axis, true);
        if (shape == null) return false;
        BlockState portal = ModContent.SKYLANDS_PORTAL.defaultBlockState().setValue(NetherPortalBlock.AXIS, axis);
        for (int x = 0; x < shape.width; x++) for (int y = 0; y < shape.height; y++)
            level.setBlock(shape.bottomLeft.relative(shape.right, x).above(y), portal, 18);
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.getDataStorage().computeIfAbsent(SkylandsPortalBlock.PORTAL_CACHE).record(shape.bottomLeft);
        }
        return true;
    }

    private static Shape find(BlockGetter level, BlockPos start, Direction.Axis axis, boolean requireEmpty) {
        Direction right = axis == Direction.Axis.X ? Direction.WEST : Direction.SOUTH;
        BlockPos bottom = start;
        while (bottom.getY() > level.getMinY() && interior(level.getBlockState(bottom.below()))) bottom = bottom.below();
        int leftEdge = edge(level, bottom, right.getOpposite());
        if (leftEdge <= 0) return null;
        BlockPos bottomLeft = bottom.relative(right.getOpposite(), leftEdge - 1);
        int width = edge(level, bottomLeft, right);
        if (width < 2 || width > 21) return null;
        int height = height(level, bottomLeft, right, width, requireEmpty);
        return height >= 3 && height <= 21 ? new Shape(bottomLeft, right, width, height) : null;
    }

    private static int edge(BlockGetter level, BlockPos start, Direction direction) {
        for (int distance = 0; distance <= 21; distance++) {
            BlockPos pos = start.relative(direction, distance);
            if (!interior(level.getBlockState(pos))) return level.getBlockState(pos).is(ModContent.GLOWING_OBSIDIAN) ? distance : 0;
            if (!level.getBlockState(pos.below()).is(ModContent.GLOWING_OBSIDIAN)) return 0;
        }
        return 0;
    }

    private static int height(BlockGetter level, BlockPos bottomLeft, Direction right, int width, boolean requireEmpty) {
        for (int height = 0; height < 21; height++) {
            if (!level.getBlockState(bottomLeft.relative(right, -1).above(height)).is(ModContent.GLOWING_OBSIDIAN)
                || !level.getBlockState(bottomLeft.relative(right, width).above(height)).is(ModContent.GLOWING_OBSIDIAN)) return height;
            for (int x = 0; x < width; x++) {
                BlockState state = level.getBlockState(bottomLeft.relative(right, x).above(height));
                if (!interior(state) || requireEmpty && state.is(ModContent.SKYLANDS_PORTAL)) return height;
            }
        }
        return 0;
    }

    private static boolean interior(BlockState state) {
        return state.isAir() || state.is(Blocks.FIRE) || state.is(ModContent.SKYLANDS_PORTAL);
    }

    private record Shape(BlockPos bottomLeft, Direction right, int width, int height) {
    }
}