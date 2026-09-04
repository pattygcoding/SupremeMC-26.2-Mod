package com.suprememc.content.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class MaterialStairsBlock extends StairBlock {
    public MaterialStairsBlock(BlockState baseState, BlockBehaviour.Properties properties) {
        super(baseState, properties);
    }

    @Override
    public MapCodec<StairBlock> codec() { return StairBlock.CODEC; }
}
