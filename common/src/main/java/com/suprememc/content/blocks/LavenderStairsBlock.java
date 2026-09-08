package com.suprememc.content.blocks;

import com.mojang.serialization.MapCodec;
import com.suprememc.content.ModContent;
import net.minecraft.world.level.block.StairBlock;

public class LavenderStairsBlock extends StairBlock {
    public LavenderStairsBlock() {
        super(ModContent.LAVENDER_PLANKS.defaultBlockState(), ModContent.blockProperties("lavender_stairs")
            .mapColor(net.minecraft.world.level.material.MapColor.COLOR_PURPLE).sound(net.minecraft.world.level.block.SoundType.WOOD).strength(2.0F, 3.0F));
    }

    @Override
    public MapCodec<StairBlock> codec() { return StairBlock.CODEC; }
}