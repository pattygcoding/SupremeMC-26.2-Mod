package com.suprememc.content.blocks;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class WetFarmlandBlock extends FarmlandBlock {
    public WetFarmlandBlock(BlockBehaviour.Properties properties) { super(properties); }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) { }
}
