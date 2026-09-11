package com.suprememc.content.blocks;

import com.suprememc.content.ModContent;
import com.suprememc.content.init.ModTreeGrowers;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class PalmSaplingBlock extends SaplingBlock {
    public PalmSaplingBlock() {
        super(ModTreeGrowers.PALM, ModContent.blockProperties("palm_sapling")
            .mapColor(net.minecraft.world.level.material.MapColor.PLANT).sound(net.minecraft.world.level.block.SoundType.GRASS).strength(0.0F).noCollision());
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState ground = level.getBlockState(pos.below());
        return super.canSurvive(state, level, pos) || ground.getBlock().asItem().builtInRegistryHolder().is(ItemTags.SAND);
    }

    @Override
    public MapCodec<SaplingBlock> codec() { return SaplingBlock.CODEC; }
}
