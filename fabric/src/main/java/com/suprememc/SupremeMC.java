package com.suprememc;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import com.suprememc.content.ModContent;
import com.suprememc.loot.ModLootInjections;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.HashMap;
import java.util.Map;

public class SupremeMC implements ModInitializer {

    @Override
    public void onInitialize() {

        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        Constants.LOG.info("Hello Fabric world!");
        CommonClass.init();
        // Vanilla's sign block entities only accept their hardcoded block list, so opt the palm signs in.
        BlockEntityTypes.SIGN.addValidBlock(ModContent.PALM_SIGN);
        BlockEntityTypes.SIGN.addValidBlock(ModContent.PALM_WALL_SIGN);
        BlockEntityTypes.HANGING_SIGN.addValidBlock(ModContent.PALM_HANGING_SIGN);
        BlockEntityTypes.HANGING_SIGN.addValidBlock(ModContent.PALM_WALL_HANGING_SIGN);
        BiomeModifications.addFeature(
            BiomeSelectors.tag(BiomeTags.IS_OCEAN),
            GenerationStep.Decoration.UNDERGROUND_ORES,
            ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "aquamarine_ore")));
        BiomeModifications.addFeature(
            BiomeSelectors.tag(BiomeTags.IS_OCEAN),
            GenerationStep.Decoration.UNDERGROUND_ORES,
            ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "atlantis_debris")));
        // Palm beaches: warm-climate beaches always grow palms; moderate-climate beaches at a 50% chunk rate
        // and lower density. The placed feature JSONs (packaged from the shared generated data) carry the
        // temperature tiers and sand substrate rules, so behavior matches the NeoForge biome modifiers.
        BiomeModifications.addFeature(
            BiomeSelectors.tag(BiomeTags.IS_BEACH),
            GenerationStep.Decoration.VEGETAL_DECORATION,
            ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "palm_trees_warm")));
        BiomeModifications.addFeature(
            BiomeSelectors.tag(BiomeTags.IS_BEACH),
            GenerationStep.Decoration.VEGETAL_DECORATION,
            ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "palm_trees_temperate")));
        BiomeModifications.addFeature(
            BiomeSelectors.includeByKey(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS),
            GenerationStep.Decoration.VEGETAL_DECORATION,
            ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "cotton_bushes")));

        CreativeModeTab tab = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                .title(Component.translatable("itemGroup." + Constants.MOD_ID + ".main"))
                .icon(() -> new ItemStack(ModContent.AQUAMARINE))
                .displayItems((params, output) -> ModContent.CREATIVE_TAB_ITEMS.forEach(output::accept))
                .build();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
                ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "main")), tab);

        registerLootInjections();
    }

    private static void registerLootInjections() {
        Map<ResourceKey<LootTable>, ResourceKey<LootTable>> injections = new HashMap<>();
        for (ModLootInjections.Injection injection : ModLootInjections.ABYSSALITE_TEMPLATE_INJECTIONS) {
            injections.put(lootTableKey(injection.targetTable()), lootTableKey(injection.injectedTable()));
        }
        for (ModLootInjections.Injection injection : ModLootInjections.CALAMARI_INJECTIONS) {
            injections.put(lootTableKey(injection.targetTable()), lootTableKey(injection.injectedTable()));
        }
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            ResourceKey<LootTable> injected = injections.get(key);
            if (injected != null && source.isBuiltin()) {
                tableBuilder.withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .add(NestedLootTable.lootTableReference(injected)));
            }
        });
    }

    private static ResourceKey<LootTable> lootTableKey(String id) {
        return ResourceKey.create(Registries.LOOT_TABLE, Identifier.parse(id));
    }
}
