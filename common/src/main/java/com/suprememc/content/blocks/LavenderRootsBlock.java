package com.suprememc.content.blocks;

import com.suprememc.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.NetherRootsBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public final class LavenderRootsBlock extends NetherRootsBlock {
    public LavenderRootsBlock(BlockBehaviour.Properties properties) {
        super(TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "supports_lavender_roots")), properties);
    }
}
