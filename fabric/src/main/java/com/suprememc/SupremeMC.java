package com.suprememc;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import com.suprememc.content.ModContent;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class SupremeMC implements ModInitializer {

    @Override
    public void onInitialize() {

        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        Constants.LOG.info("Hello Fabric world!");
        CommonClass.init();
        BiomeModifications.addFeature(
            BiomeSelectors.tag(BiomeTags.IS_OCEAN),
            GenerationStep.Decoration.UNDERGROUND_ORES,
            ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "aquamarine_ore")));
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS).register(entries -> {
            entries.accept(ModContent.AQUAMARINE);
            entries.accept(ModContent.AQUAMARINE_ORE);
            entries.accept(ModContent.DEEPSLATE_AQUAMARINE_ORE);
            entries.accept(ModContent.AQUAMARINE_BLOCK);
            entries.accept(ModContent.WET_FARMLAND);
            entries.accept(ModContent.AQUAMARINE_PICKAXE);
            entries.accept(ModContent.AQUAMARINE_AXE);
            entries.accept(ModContent.AQUAMARINE_SHOVEL);
            entries.accept(ModContent.AQUAMARINE_HOE);
            entries.accept(ModContent.AQUAMARINE_SWORD);
            entries.accept(ModContent.AQUAMARINE_HELMET);
            entries.accept(ModContent.AQUAMARINE_CHESTPLATE);
            entries.accept(ModContent.AQUAMARINE_LEGGINGS);
            entries.accept(ModContent.AQUAMARINE_BOOTS);
        });
    }
}
