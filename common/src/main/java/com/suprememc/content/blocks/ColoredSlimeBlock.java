package com.suprememc.content.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

// See MixinPistonStructureResolver: dyed slime blocks only stick to slime blocks of the same color.
public class ColoredSlimeBlock extends SlimeBlock {
    private final DyeColor color;

    public ColoredSlimeBlock(DyeColor color, BlockBehaviour.Properties properties) {
        super(properties);
        this.color = color;
    }

    public DyeColor getColor() {
        return color;
    }

    @Override
    public MapCodec<SlimeBlock> codec() {
        return MapCodec.unit(this);
    }
}
