package com.suprememc.content.blocks;

import com.suprememc.content.ModContent;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.StairBlock;

public class PalmStairsBlock extends StairBlock {
    public PalmStairsBlock() {
        super(ModContent.PALM_PLANKS.defaultBlockState(), ModContent.blockProperties("palm_stairs")
            .mapColor(net.minecraft.world.level.material.MapColor.WOOD).sound(net.minecraft.world.level.block.SoundType.WOOD).strength(2.0F, 3.0F));
    }

    @Override
    public MapCodec<StairBlock> codec() { return StairBlock.CODEC; }
}
