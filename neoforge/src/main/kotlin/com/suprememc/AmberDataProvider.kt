package com.suprememc

import com.google.gson.JsonArray
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class AmberDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    private val blocks = arrayOf("amber_ore", "deepslate_amber_ore", "amber_block")
    private val items = arrayOf("amber", "amber_pickaxe", "amber_axe", "amber_shovel", "amber_hoe", "amber_sword", "amber_helmet", "amber_chestplate", "amber_leggings", "amber_boots")

    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        blocks.forEach { writeBlock(cache, writes, it) }
        modelFiles(cache, writes, items)
        writeRecipes(cache, writes)
        writeWorldgen(cache, writes)
        writes += save(cache, oreLoot("amber_ore"), dataPath("loot_table/blocks/amber_ore.json"))
        writes += save(cache, oreLoot("deepslate_amber_ore"), dataPath("loot_table/blocks/deepslate_amber_ore.json"))
        writes += save(cache, valuesTag("$namespace:aquamarine_ore", "$namespace:deepslate_aquamarine_ore", "$namespace:aquamarine_block", "$namespace:nether_anthracite_ore", "$namespace:anthracite_block", "$namespace:amber_ore", "$namespace:deepslate_amber_ore", "$namespace:amber_block", "$namespace:prismarine_ore", "$namespace:deepslate_prismarine_ore"), minecraftDataPath("tags/block/needs_iron_tool.json"))
        writes += save(cache, valuesTag("$namespace:aquamarine_ore", "$namespace:deepslate_aquamarine_ore", "$namespace:aquamarine_block", "$namespace:atlantis_debris", "$namespace:abyssalite_block", "$namespace:nether_anthracite_ore", "$namespace:anthracite_block", "$namespace:amber_ore", "$namespace:deepslate_amber_ore", "$namespace:amber_block", "$namespace:prismarine_ore", "$namespace:deepslate_prismarine_ore"), minecraftDataPath("tags/block/mineable/pickaxe.json"))
        writes += save(cache, valuesTag("$namespace:aquamarine_block", "$namespace:amber_block"), minecraftDataPath("tags/block/beacon_base_blocks.json"))
        writes += save(cache, valuesTag("$namespace:aquamarine_ore", "$namespace:deepslate_aquamarine_ore", "$namespace:nether_anthracite_ore", "$namespace:atlantis_debris", "$namespace:amber_ore", "$namespace:deepslate_amber_ore"), cDataPath("tags/item/ores.json"))
        writes += save(cache, valuesTag("$namespace:aquamarine", "$namespace:amber"), minecraftDataPath("tags/item/beacon_payment_items.json"))
        writes += save(cache, equipmentAsset(), resourcePath("equipment/amber.json"))
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun writeBlock(cache: CachedOutput, writes: MutableList<CompletableFuture<*>>, id: String) {
        writes += save(cache, obj { add("variants", obj { add("", obj { addProperty("model", "$namespace:block/$id") }) }) }, resourcePath("blockstates/$id.json"))
        writes += save(cache, obj { addProperty("parent", "minecraft:block/cube_all"); add("textures", obj { addProperty("all", "$namespace:block/$id") }) }, resourcePath("models/block/$id.json"))
        writes += save(cache, obj { addProperty("parent", "$namespace:block/$id") }, resourcePath("models/item/$id.json"))
        writes += save(cache, itemModelDefinition("$namespace:block/$id"), resourcePath("items/$id.json"))
        if (!id.endsWith("_ore")) writes += save(cache, selfDropLootTable(id), dataPath("loot_table/blocks/$id.json"))
    }

    private fun writeRecipes(cache: CachedOutput, writes: MutableList<CompletableFuture<*>>) {
        fun shaped(id: String, pattern: Array<String>) {
            val recipe = shapedRecipe(id, "equipment", pattern, "A", "amber")
            if (pattern.joinToString("").contains('S')) recipe.getAsJsonObject("key").addProperty("S", "minecraft:stick")
            writes += save(cache, recipe, dataPath("recipe/$id.json"))
            writes += save(cache, recipeAdvancement(id, "equipment", "amber"), dataPath("advancement/recipes/equipment/$id.json"))
        }
        shaped("amber_pickaxe", arrayOf("AAA", " S ", " S ")); shaped("amber_axe", arrayOf("AA ", "AS ", " S ")); shaped("amber_shovel", arrayOf("A", "S", "S")); shaped("amber_hoe", arrayOf("AA ", " S ", " S ")); shaped("amber_sword", arrayOf("A", "A", "S"))
        shaped("amber_helmet", arrayOf("AAA", "A A")); shaped("amber_chestplate", arrayOf("A A", "AAA", "AAA")); shaped("amber_leggings", arrayOf("AAA", "A A", "A A")); shaped("amber_boots", arrayOf("A A", "A A"))
        writes += save(cache, shapedRecipe("amber_block", "building", arrayOf("AAA", "AAA", "AAA"), "A", "amber"), dataPath("recipe/amber_block.json"))
        writes += save(cache, recipeAdvancement("amber_block", "building", "amber"), dataPath("advancement/recipes/building/amber_block.json"))
        writes += save(cache, shapelessRecipe("amber", 9, "amber_block"), dataPath("recipe/amber_from_amber_block.json"))
        writes += save(cache, recipeAdvancement("amber_from_amber_block", "misc", "amber_block"), dataPath("advancement/recipes/misc/amber_from_amber_block.json"))
        listOf("amber_ore", "deepslate_amber_ore").forEach { ore ->
            writes += save(cache, cookingRecipe("minecraft:smelting", ore), dataPath("recipe/amber_from_smelting_$ore.json"))
            writes += save(cache, recipeAdvancement("amber_from_smelting_$ore", "misc", ore), dataPath("advancement/recipes/misc/amber_from_smelting_$ore.json"))
            writes += save(cache, cookingRecipe("minecraft:blasting", ore), dataPath("recipe/amber_from_blasting_$ore.json"))
            writes += save(cache, recipeAdvancement("amber_from_blasting_$ore", "misc", ore), dataPath("advancement/recipes/misc/amber_from_blasting_$ore.json"))
        }
    }

    private fun writeWorldgen(cache: CachedOutput, writes: MutableList<CompletableFuture<*>>) {
        val feature = obj {
            addProperty("type", "minecraft:ore")
            add("config", obj {
                addProperty("discard_chance_on_air_exposure", 0.5f); addProperty("size", 4)
                add("targets", JsonArray().also { it.add(oreTarget("$namespace:amber_ore", "minecraft:stone_ore_replaceables")); it.add(oreTarget("$namespace:deepslate_amber_ore", "minecraft:deepslate_ore_replaceables")); it.add(oreTarget("$namespace:deepslate_amber_ore", "minecraft:dripstone_block", false)) })
            })
        }
        writes += save(cache, feature, dataPath("worldgen/configured_feature/amber_ore.json"))
        writes += save(cache, obj { addProperty("feature", "$namespace:amber_ore"); add("placement", placement(7, -80, 80)) }, dataPath("worldgen/placed_feature/amber_ore.json"))
        writes += save(cache, obj { addProperty("feature", "$namespace:amber_ore"); add("placement", placement(14, -80, 80)) }, dataPath("worldgen/placed_feature/amber_ore_pale_garden.json"))
        writes += save(cache, obj { addProperty("type", "neoforge:add_features"); add("biomes", array("minecraft:forest", "minecraft:flower_forest", "minecraft:birch_forest", "minecraft:old_growth_birch_forest", "minecraft:dark_forest", "minecraft:grove")); addProperty("features", "$namespace:amber_ore"); addProperty("step", "underground_ores") }, dataPath("neoforge/biome_modifier/add_amber_ore.json"))
        writes += save(cache, obj { addProperty("type", "neoforge:add_features"); add("biomes", array("minecraft:pale_garden")); addProperty("features", "$namespace:amber_ore_pale_garden"); addProperty("step", "underground_ores") }, dataPath("neoforge/biome_modifier/add_amber_ore_pale_garden.json"))
    }

    private fun oreTarget(block: String, value: String, tag: Boolean = true) = obj { add("state", obj { addProperty("Name", block) }); add("target", obj { addProperty("predicate_type", if (tag) "minecraft:tag_match" else "minecraft:block_match"); addProperty(if (tag) "tag" else "block", value) }) }
    private fun placement(count: Int, min: Int, max: Int) = JsonArray().also { it.add(obj { addProperty("type", "minecraft:count"); addProperty("count", count) }); it.add(obj { addProperty("type", "minecraft:in_square") }); it.add(obj { addProperty("type", "minecraft:height_range"); add("height", obj { addProperty("type", "minecraft:trapezoid"); add("min_inclusive", obj { addProperty("above_bottom", min) }); add("max_inclusive", obj { addProperty("above_bottom", max) }) }) }); it.add(obj { addProperty("type", "minecraft:biome") }) }
    private fun oreLoot(id: String) = obj { addProperty("type", "minecraft:block"); add("pools", JsonArray().also { it.add(obj { addProperty("rolls", 1); add("entries", JsonArray().also { e -> e.add(itemLootEntry(id).apply { add("conditions", JsonArray().also { it.add(obj { addProperty("condition", "minecraft:match_tool"); add("predicate", obj { add("predicates", obj { add("minecraft:enchantments", JsonArray().also { it.add(obj { addProperty("enchantments", "minecraft:silk_touch"); add("levels", obj { addProperty("min", 1) }) }) }) }) }) }) }) }); e.add(itemLootEntry("amber").apply { add("functions", JsonArray().also { it.add(obj { addProperty("function", "minecraft:apply_bonus"); addProperty("enchantment", "minecraft:fortune"); addProperty("formula", "minecraft:ore_drops") }); it.add(obj { addProperty("function", "minecraft:explosion_decay") }) }) }) }) }) }) }
    private fun equipmentAsset() = obj { add("layers", obj { add("humanoid", arrayLayer("amber")); add("humanoid_baby", arrayLayer("amber")); add("humanoid_leggings", arrayLayer("amber")) }) }
    private fun arrayLayer(id: String) = JsonArray().also { it.add(obj { addProperty("texture", "$namespace:$id") }) }
    override fun getName() = "SupremeMC amber resources"
}