package com.suprememc.content.blocks;

import com.suprememc.Constants;
import com.suprememc.content.ModContent;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.NetherFungusBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public final class LavenderFungusBlock extends NetherFungusBlock {
    private static final ResourceKey<ConfiguredFeature<?, ?>> HUGE_LAVENDER_FUNGUS = ResourceKey.create(
        Registries.CONFIGURED_FEATURE,
        Identifier.fromNamespaceAndPath(Constants.MOD_ID, "lavender_fungus_planted")
    );

    public LavenderFungusBlock(BlockBehaviour.Properties properties) {
        super(HUGE_LAVENDER_FUNGUS, ModContent.LAVENDER_ENDSPAR,
            TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "supports_lavender_fungus")), properties);
    }
}
