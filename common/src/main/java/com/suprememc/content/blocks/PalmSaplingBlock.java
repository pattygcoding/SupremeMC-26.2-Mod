package com.suprememc.content.blocks;

import com.suprememc.content.ModContent;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class PalmSaplingBlock extends SaplingBlock {
    public PalmSaplingBlock() {
        super(ModContent.PALM_TREE_GROWER, ModContent.blockProperties("palm_sapling")
            .mapColor(net.minecraft.world.level.material.MapColor.PLANT).sound(net.minecraft.world.level.block.SoundType.GRASS).strength(0.0F).noCollision());
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState ground = level.getBlockState(pos.below());
        return super.canSurvive(state, level, pos) || ground.is(Blocks.SAND) || ground.is(Blocks.RED_SAND);
    }

    @Override
    public MapCodec<SaplingBlock> codec() { return SaplingBlock.CODEC; }
}
