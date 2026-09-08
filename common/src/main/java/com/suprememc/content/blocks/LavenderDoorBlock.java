package com.suprememc.content.blocks;

import com.mojang.serialization.MapCodec;
import com.suprememc.content.ModContent;
import com.suprememc.content.init.ModBlockSetTypes;
import net.minecraft.world.level.block.DoorBlock;

public class LavenderDoorBlock extends DoorBlock {
    public LavenderDoorBlock() {
        super(ModBlockSetTypes.LAVENDER, ModContent.blockProperties("lavender_door")
            .mapColor(net.minecraft.world.level.material.MapColor.COLOR_PURPLE).sound(net.minecraft.world.level.block.SoundType.WOOD).strength(3.0F).noOcclusion());
    }

    @Override
    public MapCodec<DoorBlock> codec() { return DoorBlock.CODEC; }
}