package com.suprememc.content.blocks;

import com.mojang.serialization.MapCodec;
import com.suprememc.content.ModContent;
import com.suprememc.content.init.ModBlockSetTypes;
import net.minecraft.world.level.block.PressurePlateBlock;

public class LavenderPressurePlateBlock extends PressurePlateBlock {
    public LavenderPressurePlateBlock() {
        super(ModBlockSetTypes.LAVENDER, ModContent.blockProperties("lavender_pressure_plate")
            .mapColor(net.minecraft.world.level.material.MapColor.COLOR_PURPLE).sound(net.minecraft.world.level.block.SoundType.WOOD).strength(0.5F));
    }

    @Override
    public MapCodec<PressurePlateBlock> codec() { return PressurePlateBlock.CODEC; }
}