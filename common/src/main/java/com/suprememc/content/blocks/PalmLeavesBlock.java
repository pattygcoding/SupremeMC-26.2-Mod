package com.suprememc.content.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class PalmLeavesBlock extends LeavesBlock {
    public PalmLeavesBlock(BlockBehaviour.Properties properties) { super(0.2F, properties); }

    @Override
    public MapCodec<PalmLeavesBlock> codec() { return MapCodec.unit(this); }

    @Override
    protected void spawnFallingLeavesParticle(Level level, BlockPos pos, RandomSource random) { }
}
