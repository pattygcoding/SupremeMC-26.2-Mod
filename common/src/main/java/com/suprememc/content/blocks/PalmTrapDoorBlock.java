package com.suprememc.content.blocks;

import com.suprememc.content.ModContent;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.TrapDoorBlock;

public class PalmTrapDoorBlock extends TrapDoorBlock {
    public PalmTrapDoorBlock() {
        super(ModContent.PALM_BLOCK_SET, ModContent.blockProperties("palm_trapdoor")
            .mapColor(net.minecraft.world.level.material.MapColor.WOOD).sound(net.minecraft.world.level.block.SoundType.WOOD).strength(3.0F).noOcclusion());
    }

    @Override
    public MapCodec<TrapDoorBlock> codec() { return TrapDoorBlock.CODEC; }
}
