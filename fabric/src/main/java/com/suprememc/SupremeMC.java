package com.suprememc;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.registry.FuelValueEvents;
import net.fabricmc.fabric.api.registry.FabricPotionBrewingBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.biome.Biome;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class SupremeMC implements ModInitializer {

    private static final ResourceKey<Biome> CAYS =
        ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "cays"));

    @Override
    public void onInitialize() {

        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        Constants.LOG.info("Hello Fabric world!");
        CommonClass.init();
        FabricPotionBrewingBuilder.BUILD.register(builder -> {
            builder.registerPotionRecipe(net.minecraft.world.item.alchemy.Potions.AWKWARD,
                Ingredient.of(ModContent.CLOVER.asItem()), ModContent.LUCK_POTION);
            builder.registerPotionRecipe(ModContent.LUCK_POTION, Ingredient.of(Items.REDSTONE), ModContent.LONG_LUCK_POTION);
            builder.registerPotionRecipe(ModContent.LUCK_POTION, Ingredient.of(Items.GLOWSTONE_DUST), ModContent.STRONG_LUCK_POTION);
            builder.registerPotionRecipe(ModContent.LUCK_POTION, Ingredient.of(Items.FERMENTED_SPIDER_EYE), ModContent.BAD_LUCK_POTION);
            builder.registerPotionRecipe(ModContent.LONG_LUCK_POTION, Ingredient.of(Items.FERMENTED_SPIDER_EYE), ModContent.LONG_BAD_LUCK_POTION);
            builder.registerPotionRecipe(ModContent.STRONG_LUCK_POTION, Ingredient.of(Items.FERMENTED_SPIDER_EYE), ModContent.STRONG_BAD_LUCK_POTION);
                builder.registerPotionRecipe(Potions.AWKWARD, Ingredient.of(Items.ROTTEN_FLESH), ModContent.HUNGER_POTION);
                builder.registerPotionRecipe(ModContent.HUNGER_POTION, Ingredient.of(Items.REDSTONE), ModContent.LONG_HUNGER_POTION);
                builder.registerPotionRecipe(ModContent.HUNGER_POTION, Ingredient.of(Items.GLOWSTONE_DUST), ModContent.STRONG_HUNGER_POTION);
                builder.registerPotionRecipe(Potions.AWKWARD, Ingredient.of(Items.WITHER_ROSE), ModContent.DECAY_POTION);
                builder.registerPotionRecipe(ModContent.DECAY_POTION, Ingredient.of(Items.REDSTONE), ModContent.LONG_DECAY_POTION);
                builder.registerPotionRecipe(ModContent.DECAY_POTION, Ingredient.of(Items.GLOWSTONE_DUST), ModContent.STRONG_DECAY_POTION);
        });
        FuelValueEvents.BUILD.register((builder, context) -> {
            builder.add(ModContent.ANTHRACITE, 1600);
            builder.add(ModContent.ANTHRACITE_BLOCK, 16000);
        });
        FabricDefaultAttributeRegistry.register(ModContent.GRIZZLY_BEAR_ENTITY,
            net.minecraft.world.entity.animal.polarbear.PolarBear.createAttributes());
        FabricDefaultAttributeRegistry.register(ModContent.ENDER_SPIDER_ENTITY,
            com.suprememc.content.entity.EnderSpider.createAttributes());
        FabricDefaultAttributeRegistry.register(ModContent.FIRE_CREEPER_ENTITY,
            net.minecraft.world.entity.monster.Creeper.createAttributes());
        FabricDefaultAttributeRegistry.register(ModContent.SNOW_CREEPER_ENTITY,
            net.minecraft.world.entity.monster.Creeper.createAttributes());
        // Vanilla's sign block entities only accept their hardcoded block list, so opt the palm signs in.
        BlockEntityTypes.SIGN.addValidBlock(ModContent.PALM_SIGN);
        BlockEntityTypes.SIGN.addValidBlock(ModContent.PALM_WALL_SIGN);
        BlockEntityTypes.HANGING_SIGN.addValidBlock(ModContent.PALM_HANGING_SIGN);
        BlockEntityTypes.HANGING_SIGN.addValidBlock(ModContent.PALM_WALL_HANGING_SIGN);
        BlockEntityTypes.FURNACE.addValidBlock(ModContent.BLACKSTONE_FURNACE);
        BlockEntityTypes.FURNACE.addValidBlock(ModContent.DEEPSLATE_FURNACE);
        BiomeModifications.addFeature(
            BiomeSelectors.tag(BiomeTags.IS_OCEAN),
            GenerationStep.Decoration.UNDERGROUND_ORES,
            ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "aquamarine_ore")));
        BiomeModifications.addFeature(
            BiomeSelectors.tag(BiomeTags.IS_OCEAN),
            GenerationStep.Decoration.UNDERGROUND_ORES,
            ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "prismarine_ore")));
        BiomeModifications.addFeature(
            BiomeSelectors.tag(BiomeTags.IS_OCEAN),
            GenerationStep.Decoration.UNDERGROUND_ORES,
            ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "prismarine_ore_small")));
        BiomeModifications.addFeature(
            BiomeSelectors.tag(BiomeTags.IS_OCEAN),
            GenerationStep.Decoration.UNDERGROUND_ORES,
            ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "atlantis_debris")));
        BiomeModifications.addFeature(
            BiomeSelectors.includeByKey(Biomes.FOREST, Biomes.FLOWER_FOREST, Biomes.BIRCH_FOREST,
                Biomes.OLD_GROWTH_BIRCH_FOREST, Biomes.DARK_FOREST, Biomes.GROVE),
            GenerationStep.Decoration.UNDERGROUND_ORES,
            ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "amber_ore")));
        BiomeModifications.addFeature(
            BiomeSelectors.includeByKey(Biomes.PALE_GARDEN),
            GenerationStep.Decoration.UNDERGROUND_ORES,
            ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "amber_ore_pale_garden")));
        BiomeModifications.addFeature(
            BiomeSelectors.tag(BiomeTags.IS_NETHER),
            GenerationStep.Decoration.UNDERGROUND_ORES,
            ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "nether_anthracite_ore")));
        BiomeModifications.addFeature(
            BiomeSelectors.tag(BiomeTags.IS_NETHER),
            GenerationStep.Decoration.UNDERGROUND_ORES,
            ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "burning_diamond_ore")));
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
        BiomeModifications.addFeature(
            BiomeSelectors.includeByKey(Biomes.PLAINS, Biomes.SUNFLOWER_PLAINS),
            GenerationStep.Decoration.VEGETAL_DECORATION,
            ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "corn_patches")));
        // Beach grass grows on both real beach biomes and the custom Cays biome (mirrors the NeoForge biome modifier).
        Predicate<BiomeSelectionContext> beachOrCays =
            BiomeSelectors.tag(BiomeTags.IS_BEACH).or(BiomeSelectors.includeByKey(CAYS));
        BiomeModifications.addFeature(
            beachOrCays,
            GenerationStep.Decoration.VEGETAL_DECORATION,
            ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "beach_grass")));
        BiomeModifications.addFeature(
            beachOrCays,
            GenerationStep.Decoration.VEGETAL_DECORATION,
            ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "tall_beach_grass")));
        BiomeModifications.addFeature(
            BiomeSelectors.tag(BiomeTags.IS_SAVANNA),
            GenerationStep.Decoration.VEGETAL_DECORATION,
            ResourceKey.create(Registries.PLACED_FEATURE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "grape_vine_hang")));
        registerDrownedSpawns();
        registerEnderSpiderSpawns();
        registerFireCreeperSpawns();
        registerSnowCreeperSpawns();

        CreativeModeTab tab = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                .title(Component.translatable("itemGroup." + Constants.MOD_ID + ".main"))
                .icon(() -> new ItemStack(ModContent.SUPREME_MC_LOGO_BLOCK))
                .displayItems((params, output) -> ModContent.CREATIVE_TAB_ITEMS.forEach(output::accept))
                .build();
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB,
                ResourceKey.create(Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "main")), tab);

        registerLootInjections();
    }

        private static void registerDrownedSpawns() {
        Predicate<BiomeSelectionContext> river = BiomeSelectors.includeByKey(Biomes.RIVER);
        Predicate<BiomeSelectionContext> frozenRiver = BiomeSelectors.includeByKey(Biomes.FROZEN_RIVER);
        Predicate<BiomeSelectionContext> dripstoneCaves = BiomeSelectors.includeByKey(Biomes.DRIPSTONE_CAVES);
        Predicate<BiomeSelectionContext> iceCaves = BiomeSelectors.includeByKey(ResourceKey.create(
            Registries.BIOME, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "ice_caves")));
        Predicate<BiomeSelectionContext> oceans = BiomeSelectors.tag(BiomeTags.IS_OCEAN);

        BiomeModifications.create(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "drowned_spawns"))
            .add(net.fabricmc.fabric.api.biome.v1.ModificationPhase.REPLACEMENTS, river,
                context -> replaceDrownedSpawn(context, 100, 1, 1))
            .add(net.fabricmc.fabric.api.biome.v1.ModificationPhase.REPLACEMENTS, dripstoneCaves,
                context -> replaceDrownedSpawn(context, 100, 4, 4))
            .add(net.fabricmc.fabric.api.biome.v1.ModificationPhase.REPLACEMENTS, iceCaves,
                context -> replaceDrownedSpawn(context, 100, 4, 4))
            .add(net.fabricmc.fabric.api.biome.v1.ModificationPhase.REPLACEMENTS, oceans,
                context -> replaceDrownedSpawn(context, 100, 1, 1))
            .add(net.fabricmc.fabric.api.biome.v1.ModificationPhase.REPLACEMENTS, frozenRiver,
                context -> replaceDrownedSpawn(context, 5, 1, 1));
        }

        private static void replaceDrownedSpawn(net.fabricmc.fabric.api.biome.v1.BiomeModificationContext context,
                            int weight, int minCount, int maxCount) {
            EntityType<?> drowned = BuiltInRegistries.ENTITY_TYPE.get(
                Identifier.fromNamespaceAndPath("minecraft", "drowned")).orElseThrow().value();
            context.getMobSpawnSettings().removeSpawnsOfEntityType(drowned);
        context.getMobSpawnSettings().addSpawn(
            MobCategory.MONSTER,
                new net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData(drowned, minCount, maxCount),
            weight);
        }

        private static void registerFireCreeperSpawns() {
        BiomeModifications.create(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "fire_creeper_spawns"))
            .add(net.fabricmc.fabric.api.biome.v1.ModificationPhase.ADDITIONS,
                BiomeSelectors.includeByKey(
                    Biomes.NETHER_WASTES,
                    Biomes.SOUL_SAND_VALLEY,
                    Biomes.CRIMSON_FOREST,
                    Biomes.BASALT_DELTAS),
                context -> context.getMobSpawnSettings().addSpawn(
                    MobCategory.MONSTER,
                    new net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData(
                        ModContent.FIRE_CREEPER_ENTITY, 1, 2),
                    25));
        }

        private static void registerEnderSpiderSpawns() {
        BiomeModifications.create(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "ender_spider_spawns"))
            .add(net.fabricmc.fabric.api.biome.v1.ModificationPhase.ADDITIONS,
                BiomeSelectors.includeByKey(
                    Biomes.THE_END,
                    Biomes.END_HIGHLANDS,
                    Biomes.END_MIDLANDS,
                    Biomes.SMALL_END_ISLANDS,
                    Biomes.END_BARRENS),
                context -> context.getMobSpawnSettings().addSpawn(
                    MobCategory.MONSTER,
                    new net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData(
                        ModContent.ENDER_SPIDER_ENTITY, 1, 4),
                    3));
        }

        private static void registerSnowCreeperSpawns() {
        BiomeModifications.create(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "snow_creeper_spawns"))
            .add(net.fabricmc.fabric.api.biome.v1.ModificationPhase.REPLACEMENTS,
                BiomeSelectors.includeByKey(
                    Biomes.SNOWY_PLAINS,
                    Biomes.ICE_SPIKES,
                    Biomes.GROVE,
                    Biomes.SNOWY_SLOPES,
                    Biomes.FROZEN_PEAKS,
                    Biomes.JAGGED_PEAKS,
                    Biomes.SNOWY_BEACH,
                    Biomes.WINDSWEPT_HILLS,
                    Biomes.WINDSWEPT_FOREST,
                    Biomes.WINDSWEPT_GRAVELLY_HILLS,
                    Biomes.STONY_PEAKS,
                    Biomes.TAIGA,
                    Biomes.SNOWY_TAIGA,
                    Biomes.OLD_GROWTH_PINE_TAIGA,
                    Biomes.OLD_GROWTH_SPRUCE_TAIGA),
                context -> {
                    EntityType<?> creeper = BuiltInRegistries.ENTITY_TYPE.get(
                        Identifier.fromNamespaceAndPath("minecraft", "creeper")).orElseThrow().value();
                    context.getMobSpawnSettings().removeSpawnsOfEntityType(creeper);
                    context.getMobSpawnSettings().addSpawn(
                        MobCategory.MONSTER,
                        new net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData(
                            ModContent.SNOW_CREEPER_ENTITY, 4, 4),
                        100);
                });
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
