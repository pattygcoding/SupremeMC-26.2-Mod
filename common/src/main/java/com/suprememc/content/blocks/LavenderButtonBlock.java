package com.suprememc.content.blocks;

import com.mojang.serialization.MapCodec;
import com.suprememc.content.ModContent;
import com.suprememc.content.init.ModBlockSetTypes;
import net.minecraft.world.level.block.ButtonBlock;

public class LavenderButtonBlock extends ButtonBlock {
    public LavenderButtonBlock() {
        super(ModBlockSetTypes.LAVENDER, 30, ModContent.blockProperties("lavender_button")
            .mapColor(net.minecraft.world.level.material.MapColor.COLOR_PURPLE).sound(net.minecraft.world.level.block.SoundType.WOOD).strength(0.5F).noCollision().noOcclusion());
    }

    @Override
    public MapCodec<ButtonBlock> codec() { return ButtonBlock.CODEC; }
}