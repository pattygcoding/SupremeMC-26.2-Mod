package com.suprememc;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class SupremeMCDataProvider implements DataProvider {

    private static final String NAMESPACE = Constants.MOD_ID;
    private final PackOutput output;

    public SupremeMCDataProvider(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> writes = new ArrayList<>();
        writeBlockResources(cache, writes, "aquamarine_ore");
        writeBlockResources(cache, writes, "deepslate_aquamarine_ore");
        writeBlockResources(cache, writes, "aquamarine_block");
        writeBlockResources(cache, writes, "wet_farmland");
        writes.add(save(cache, aquamarineBlockRecipe(), dataPath("recipe/aquamarine_block.json")));
        writes.add(save(cache, decompressedAquamarineRecipe(), dataPath("recipe/aquamarine_from_aquamarine_block.json")));
        writeEquipmentRecipes(cache, writes);
        writeOreCookingRecipes(cache, writes, "aquamarine_ore");
        writeOreCookingRecipes(cache, writes, "deepslate_aquamarine_ore");
        writes.add(save(cache, aquamarineOreConfiguredFeature(), dataPath("worldgen/configured_feature/aquamarine_ore.json")));
        writes.add(save(cache, aquamarineOrePlacedFeature(), dataPath("worldgen/placed_feature/aquamarine_ore.json")));
        writes.add(save(cache, aquamarineOreBiomeModifier(), dataPath("neoforge/biome_modifier/add_aquamarine_ore.json")));
        writes.add(save(cache, oreLootTable("aquamarine_ore"), dataPath("loot_table/blocks/aquamarine_ore.json")));
        writes.add(save(cache, oreLootTable("deepslate_aquamarine_ore"), dataPath("loot_table/blocks/deepslate_aquamarine_ore.json")));
        writes.add(save(cache, valuesTag("suprememc:aquamarine_ore", "suprememc:deepslate_aquamarine_ore", "suprememc:aquamarine_block"), minecraftDataPath("tags/block/needs_iron_tool.json")));
        writes.add(save(cache, valuesTag("suprememc:aquamarine_ore", "suprememc:deepslate_aquamarine_ore", "suprememc:aquamarine_block"), minecraftDataPath("tags/block/mineable/pickaxe.json")));
        writes.add(save(cache, valuesTag("suprememc:aquamarine_block"), minecraftDataPath("tags/block/beacon_base_blocks.json")));
        writes.add(save(cache, valuesTag("suprememc:aquamarine"), minecraftDataPath("tags/item/beacon_payment_items.json")));
        writes.add(save(cache, aquamarineEquipmentAsset(), resourcePath("equipment/aquamarine.json")));

        String[] itemIds = {
                "aquamarine", "aquamarine_pickaxe", "aquamarine_axe", "aquamarine_shovel",
                "aquamarine_hoe", "aquamarine_sword", "aquamarine_helmet", "aquamarine_chestplate",
                "aquamarine_leggings", "aquamarine_boots"
        };
        for (String itemId : itemIds) {
            JsonObject model = new JsonObject();
            model.addProperty("parent", "minecraft:item/generated");
            JsonObject textures = new JsonObject();
            textures.addProperty("layer0", NAMESPACE + ":item/" + itemId);
            model.add("textures", textures);
            writes.add(save(cache, model, resourcePath("models/item/" + itemId + ".json")));
        }

        JsonObject lang = new JsonObject();
        lang.addProperty("itemGroup." + NAMESPACE + ".main", "SupremeMC");
        for (String itemId : itemIds) {
            lang.addProperty("item." + NAMESPACE + "." + itemId, displayName(itemId));
        }
        lang.addProperty("block." + NAMESPACE + ".aquamarine_ore", "Aquamarine Ore");
        lang.addProperty("block." + NAMESPACE + ".deepslate_aquamarine_ore", "Deepslate Aquamarine Ore");
        lang.addProperty("block." + NAMESPACE + ".aquamarine_block", "Block of Aquamarine");
        lang.addProperty("block." + NAMESPACE + ".wet_farmland", "Wet Farmland");
        writes.add(save(cache, lang, resourcePath("lang/en_us.json")));

        return CompletableFuture.allOf(writes.toArray(CompletableFuture[]::new));
    }

    private void writeBlockResources(CachedOutput cache, List<CompletableFuture<?>> writes, String blockId) {
        JsonObject blockState = new JsonObject();
        JsonObject variants = new JsonObject();
        JsonObject variant = new JsonObject();
        variant.addProperty("model", NAMESPACE + ":block/" + blockId);
        variants.add("", variant);
        blockState.add("variants", variants);
        writes.add(save(cache, blockState, resourcePath("blockstates/" + blockId + ".json")));

        JsonObject blockModel = new JsonObject();
        if ("wet_farmland".equals(blockId)) {
            blockModel.addProperty("parent", "minecraft:block/farmland_moist");
        } else {
            blockModel.addProperty("parent", "minecraft:block/cube_all");
            JsonObject blockTextures = new JsonObject();
            blockTextures.addProperty("all", NAMESPACE + ":block/" + blockId);
            blockModel.add("textures", blockTextures);
        }
        writes.add(save(cache, blockModel, resourcePath("models/block/" + blockId + ".json")));

        JsonObject itemModel = new JsonObject();
        itemModel.addProperty("parent", NAMESPACE + ":block/" + blockId);
        writes.add(save(cache, itemModel, resourcePath("models/item/" + blockId + ".json")));

        if (!blockId.endsWith("_ore")) {
            writes.add(save(cache, selfDropLootTable(blockId), dataPath("loot_table/blocks/" + blockId + ".json")));
        }
    }

    private CompletableFuture<?> save(CachedOutput cache, JsonObject json, Path path) {
        return DataProvider.saveStable(cache, json, path);
    }

    private Path resourcePath(String relative) {
        return output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                .resolve(NAMESPACE).resolve(relative);
    }

    private Path dataPath(String relative) {
        return output.getOutputFolder(PackOutput.Target.DATA_PACK)
                .resolve(NAMESPACE).resolve(relative);
    }

    private Path minecraftDataPath(String relative) {
        return output.getOutputFolder(PackOutput.Target.DATA_PACK)
                .resolve("minecraft").resolve(relative);
    }

    private JsonObject aquamarineBlockRecipe() {
        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shaped");
        recipe.addProperty("category", "building");
        JsonArray pattern = new JsonArray();
        pattern.add("AAA");
        pattern.add("AAA");
        pattern.add("AAA");
        recipe.add("pattern", pattern);
        JsonObject key = new JsonObject();
        key.addProperty("A", NAMESPACE + ":aquamarine");
        recipe.add("key", key);
        JsonObject result = new JsonObject();
        result.addProperty("id", NAMESPACE + ":aquamarine_block");
        result.addProperty("count", 1);
        recipe.add("result", result);
        return recipe;
    }

    private JsonObject decompressedAquamarineRecipe() {
        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shapeless");
        recipe.addProperty("category", "misc");
        JsonArray ingredients = new JsonArray();
        ingredients.add(NAMESPACE + ":aquamarine_block");
        recipe.add("ingredients", ingredients);
        recipe.add("result", itemResult("aquamarine", 9));
        return recipe;
    }

    private void writeEquipmentRecipes(CachedOutput cache, List<CompletableFuture<?>> writes) {
        writeShapedRecipe(cache, writes, "aquamarine_pickaxe", "equipment", new String[] { "AAA", " S ", " S " });
        writeShapedRecipe(cache, writes, "aquamarine_axe", "equipment", new String[] { "AA ", "AS ", " S " });
        writeShapedRecipe(cache, writes, "aquamarine_shovel", "equipment", new String[] { "A", "S", "S" });
        writeShapedRecipe(cache, writes, "aquamarine_hoe", "equipment", new String[] { "AA ", " S ", " S " });
        writeShapedRecipe(cache, writes, "aquamarine_sword", "combat", new String[] { "A", "A", "S" });
        writeShapedRecipe(cache, writes, "aquamarine_helmet", "combat", new String[] { "AAA", "A A" });
        writeShapedRecipe(cache, writes, "aquamarine_chestplate", "combat", new String[] { "A A", "AAA", "AAA" });
        writeShapedRecipe(cache, writes, "aquamarine_leggings", "combat", new String[] { "AAA", "A A", "A A" });
        writeShapedRecipe(cache, writes, "aquamarine_boots", "combat", new String[] { "A A", "A A" });
    }

    private void writeShapedRecipe(CachedOutput cache, List<CompletableFuture<?>> writes, String resultId, String category, String[] pattern) {
        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", "minecraft:crafting_shaped");
        recipe.addProperty("category", category);
        JsonArray patternJson = new JsonArray();
        for (String row : pattern) {
            patternJson.add(row);
        }
        recipe.add("pattern", patternJson);
        JsonObject key = new JsonObject();
        key.addProperty("A", NAMESPACE + ":aquamarine");
        if (String.join("", pattern).contains("S")) {
            key.addProperty("S", "minecraft:stick");
        }
        recipe.add("key", key);
        recipe.add("result", itemResult(resultId, 1));
        writes.add(save(cache, recipe, dataPath("recipe/" + resultId + ".json")));
    }

    private void writeOreCookingRecipes(CachedOutput cache, List<CompletableFuture<?>> writes, String oreId) {
        writes.add(save(cache, cookingRecipe("minecraft:smelting", oreId), dataPath("recipe/aquamarine_from_smelting_" + oreId + ".json")));
        writes.add(save(cache, cookingRecipe("minecraft:blasting", oreId), dataPath("recipe/aquamarine_from_blasting_" + oreId + ".json")));
    }

    private JsonObject cookingRecipe(String type, String ingredient) {
        JsonObject recipe = new JsonObject();
        recipe.addProperty("type", type);
        recipe.addProperty("group", "aquamarine");
        recipe.addProperty("ingredient", NAMESPACE + ":" + ingredient);
        recipe.add("result", itemResult("aquamarine", 1));
        recipe.addProperty("experience", 1.0F);
        return recipe;
    }

    private JsonObject aquamarineOreConfiguredFeature() {
        JsonObject feature = new JsonObject();
        feature.addProperty("type", "minecraft:ore");
        JsonObject config = new JsonObject();
        config.addProperty("discard_chance_on_air_exposure", 0.5F);
        config.addProperty("size", 4);
        JsonArray targets = new JsonArray();
        targets.add(oreTarget("suprememc:aquamarine_ore", "minecraft:tag_match", "tag", "minecraft:stone_ore_replaceables"));
        targets.add(oreTarget("suprememc:deepslate_aquamarine_ore", "minecraft:tag_match", "tag", "minecraft:deepslate_ore_replaceables"));
        targets.add(oreTarget("suprememc:deepslate_aquamarine_ore", "minecraft:block_match", "block", "minecraft:dripstone_block"));
        config.add("targets", targets);
        feature.add("config", config);
        return feature;
    }

    private JsonObject oreTarget(String block, String predicateType, String predicateValueName, String predicateValue) {
        JsonObject target = new JsonObject();
        JsonObject state = new JsonObject();
        state.addProperty("Name", block);
        target.add("state", state);
        JsonObject predicate = new JsonObject();
        predicate.addProperty("predicate_type", predicateType);
        predicate.addProperty(predicateValueName, predicateValue);
        target.add("target", predicate);
        return target;
    }

    private JsonObject aquamarineOrePlacedFeature() {
        JsonObject feature = new JsonObject();
        feature.addProperty("feature", NAMESPACE + ":aquamarine_ore");
        JsonArray placement = new JsonArray();
        JsonObject count = new JsonObject();
        count.addProperty("type", "minecraft:count");
        count.addProperty("count", 7);
        placement.add(count);
        JsonObject inSquare = new JsonObject();
        inSquare.addProperty("type", "minecraft:in_square");
        placement.add(inSquare);
        JsonObject heightRange = new JsonObject();
        heightRange.addProperty("type", "minecraft:height_range");
        JsonObject height = new JsonObject();
        height.addProperty("type", "minecraft:trapezoid");
        JsonObject minimum = new JsonObject();
        minimum.addProperty("above_bottom", -80);
        JsonObject maximum = new JsonObject();
        maximum.addProperty("above_bottom", 80);
        height.add("min_inclusive", minimum);
        height.add("max_inclusive", maximum);
        heightRange.add("height", height);
        placement.add(heightRange);
        JsonObject biome = new JsonObject();
        biome.addProperty("type", "minecraft:biome");
        placement.add(biome);
        feature.add("placement", placement);
        return feature;
    }

    private JsonObject aquamarineOreBiomeModifier() {
        JsonObject modifier = new JsonObject();
        modifier.addProperty("type", "neoforge:add_features");
        modifier.addProperty("biomes", "#minecraft:is_ocean");
        modifier.addProperty("features", NAMESPACE + ":aquamarine_ore");
        modifier.addProperty("step", "underground_ores");
        return modifier;
    }

    private JsonObject oreLootTable(String oreId) {
        JsonObject loot = new JsonObject();
        loot.addProperty("type", "minecraft:block");
        JsonObject alternatives = new JsonObject();
        alternatives.addProperty("type", "minecraft:alternatives");
        JsonArray children = new JsonArray();
        JsonObject silkTouchDrop = itemLootEntry(oreId);
        JsonObject silkTouchCondition = new JsonObject();
        silkTouchCondition.addProperty("condition", "minecraft:match_tool");
        JsonObject predicate = new JsonObject();
        JsonObject predicates = new JsonObject();
        JsonArray enchantments = new JsonArray();
        JsonObject silkTouch = new JsonObject();
        silkTouch.addProperty("enchantments", "minecraft:silk_touch");
        JsonObject levels = new JsonObject();
        levels.addProperty("min", 1);
        silkTouch.add("levels", levels);
        enchantments.add(silkTouch);
        predicates.add("minecraft:enchantments", enchantments);
        predicate.add("predicates", predicates);
        silkTouchCondition.add("predicate", predicate);
        JsonArray conditions = new JsonArray();
        conditions.add(silkTouchCondition);
        silkTouchDrop.add("conditions", conditions);
        children.add(silkTouchDrop);
        JsonObject aquamarineDrop = itemLootEntry("aquamarine");
        JsonArray functions = new JsonArray();
        JsonObject fortune = new JsonObject();
        fortune.addProperty("function", "minecraft:apply_bonus");
        fortune.addProperty("enchantment", "minecraft:fortune");
        fortune.addProperty("formula", "minecraft:ore_drops");
        functions.add(fortune);
        JsonObject explosionDecay = new JsonObject();
        explosionDecay.addProperty("function", "minecraft:explosion_decay");
        functions.add(explosionDecay);
        aquamarineDrop.add("functions", functions);
        children.add(aquamarineDrop);
        alternatives.add("children", children);
        JsonObject pool = new JsonObject();
        pool.addProperty("rolls", 1);
        JsonArray entries = new JsonArray();
        entries.add(alternatives);
        pool.add("entries", entries);
        JsonArray pools = new JsonArray();
        pools.add(pool);
        loot.add("pools", pools);
        loot.addProperty("random_sequence", NAMESPACE + ":blocks/" + oreId);
        return loot;
    }

    private JsonObject selfDropLootTable(String blockId) {
        JsonObject loot = new JsonObject();
        loot.addProperty("type", "minecraft:block");
        JsonObject pool = new JsonObject();
        pool.addProperty("rolls", 1);
        JsonArray entries = new JsonArray();
        entries.add(itemLootEntry(blockId));
        pool.add("entries", entries);
        JsonArray pools = new JsonArray();
        pools.add(pool);
        loot.add("pools", pools);
        return loot;
    }

    private JsonObject itemLootEntry(String id) {
        JsonObject entry = new JsonObject();
        entry.addProperty("type", "minecraft:item");
        entry.addProperty("name", NAMESPACE + ":" + id);
        return entry;
    }

    private JsonObject valuesTag(String... values) {
        JsonObject tag = new JsonObject();
        JsonArray entries = new JsonArray();
        for (String value : values) {
            entries.add(value);
        }
        tag.add("values", entries);
        return tag;
    }

    private JsonObject itemResult(String id, int count) {
        JsonObject result = new JsonObject();
        result.addProperty("id", NAMESPACE + ":" + id);
        result.addProperty("count", count);
        return result;
    }

    private JsonObject aquamarineEquipmentAsset() {
        JsonObject asset = new JsonObject();
        JsonObject layers = new JsonObject();
        layers.add("humanoid", aquamarineEquipmentLayer());
        layers.add("humanoid_baby", aquamarineEquipmentLayer());
        layers.add("humanoid_leggings", aquamarineEquipmentLayer());
        asset.add("layers", layers);
        return asset;
    }

    private JsonArray aquamarineEquipmentLayer() {
        JsonObject layer = new JsonObject();
        layer.addProperty("texture", NAMESPACE + ":aquamarine");
        JsonArray layers = new JsonArray();
        layers.add(layer);
        return layers;
    }

    private static String displayName(String id) {
        String[] words = id.split("_");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (!result.isEmpty()) {
                result.append(' ');
            }
            result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
        }
        return result.toString();
    }

    @Override
    public String getName() {
        return "SupremeMC resources";
    }
}