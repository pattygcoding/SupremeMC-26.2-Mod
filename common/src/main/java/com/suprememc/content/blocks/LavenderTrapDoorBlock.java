package com.suprememc.content.blocks;

import com.mojang.serialization.MapCodec;
import com.suprememc.content.ModContent;
import com.suprememc.content.init.ModBlockSetTypes;
import net.minecraft.world.level.block.TrapDoorBlock;

public class LavenderTrapDoorBlock extends TrapDoorBlock {
    public LavenderTrapDoorBlock() {
        super(ModBlockSetTypes.LAVENDER, ModContent.blockProperties("lavender_trapdoor")
            .mapColor(net.minecraft.world.level.material.MapColor.COLOR_PURPLE).sound(net.minecraft.world.level.block.SoundType.WOOD).strength(3.0F).noOcclusion());
    }

    @Override
    public MapCodec<TrapDoorBlock> codec() { return TrapDoorBlock.CODEC; }
}