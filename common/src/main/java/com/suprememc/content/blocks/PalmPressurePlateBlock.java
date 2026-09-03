package com.suprememc.content.blocks;

import com.suprememc.content.ModContent;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.PressurePlateBlock;

public class PalmPressurePlateBlock extends PressurePlateBlock {
    public PalmPressurePlateBlock() {
        super(ModContent.PALM_BLOCK_SET, ModContent.blockProperties("palm_pressure_plate")
            .mapColor(net.minecraft.world.level.material.MapColor.WOOD).sound(net.minecraft.world.level.block.SoundType.WOOD).strength(0.5F));
    }

    @Override
    public MapCodec<PressurePlateBlock> codec() { return PressurePlateBlock.CODEC; }
}
