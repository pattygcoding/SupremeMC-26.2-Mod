package com.suprememc.content.blocks;

import com.suprememc.content.ModContent;
import com.suprememc.content.init.ModBlockSetTypes;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.ButtonBlock;

public class PalmButtonBlock extends ButtonBlock {
    public PalmButtonBlock() {
        super(ModBlockSetTypes.PALM, 30, ModContent.blockProperties("palm_button")
            .mapColor(net.minecraft.world.level.material.MapColor.WOOD).sound(net.minecraft.world.level.block.SoundType.WOOD).strength(0.5F).noCollision().noOcclusion());
    }

    @Override
    public MapCodec<ButtonBlock> codec() { return ButtonBlock.CODEC; }
}
