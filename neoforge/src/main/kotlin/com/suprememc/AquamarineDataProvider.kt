package com.suprememc

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class AquamarineDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    private val blocks = arrayOf("aquamarine_ore", "deepslate_aquamarine_ore", "aquamarine_block", "wet_farmland")
    private val items = arrayOf("aquamarine", "aquamarine_pickaxe", "aquamarine_axe", "aquamarine_shovel", "aquamarine_hoe", "aquamarine_sword", "aquamarine_helmet", "aquamarine_chestplate", "aquamarine_leggings", "aquamarine_boots")

    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        blocks.forEach { writeBlock(cache, writes, it) }
        modelFiles(cache, writes, items)
        writeRecipes(cache, writes)
        writeWorldgen(cache, writes)
        writes += save(cache, oreLoot("aquamarine_ore"), dataPath("loot_table/blocks/aquamarine_ore.json"))
        writes += save(cache, oreLoot("deepslate_aquamarine_ore"), dataPath("loot_table/blocks/deepslate_aquamarine_ore.json"))
        writes += save(cache, valuesTag("$namespace:aquamarine_ore", "$namespace:deepslate_aquamarine_ore", "$namespace:aquamarine_block", "$namespace:nether_anthracite_ore", "$namespace:anthracite_block", "$namespace:burning_diamond_ore", "$namespace:burning_diamond_block"), minecraftDataPath("tags/block/needs_iron_tool.json"))
        writes += save(cache, valuesTag("$namespace:aquamarine_ore", "$namespace:deepslate_aquamarine_ore", "$namespace:aquamarine_block", "$namespace:atlantis_debris", "$namespace:abyssalite_block", "$namespace:nether_anthracite_ore", "$namespace:anthracite_block", "$namespace:burning_diamond_ore", "$namespace:burning_diamond_block"), minecraftDataPath("tags/block/mineable/pickaxe.json"))
        writes += save(cache, valuesTag("$namespace:aquamarine_block"), minecraftDataPath("tags/block/beacon_base_blocks.json"))
        writes += save(cache, valuesTag("$namespace:aquamarine_ore", "$namespace:deepslate_aquamarine_ore", "$namespace:nether_anthracite_ore", "$namespace:atlantis_debris"), cDataPath("tags/item/ores.json"))
        writes += save(cache, valuesTag("$namespace:aquamarine"), minecraftDataPath("tags/item/beacon_payment_items.json"))
        writes += save(cache, equipmentAsset(), resourcePath("equipment/aquamarine.json"))
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun writeBlock(cache: CachedOutput, writes: MutableList<CompletableFuture<*>>, id: String) {
        writes += save(cache, obj { add("variants", obj { add("", obj { addProperty("model", "$namespace:block/$id") }) }) }, resourcePath("blockstates/$id.json"))
        val model = obj { addProperty("parent", if (id == "wet_farmland") "minecraft:block/farmland_moist" else "minecraft:block/cube_all") }
        if (id != "wet_farmland") model.add("textures", obj { addProperty("all", "$namespace:block/$id") })
        writes += save(cache, model, resourcePath("models/block/$id.json"))
        writes += save(cache, obj { addProperty("parent", "$namespace:block/$id") }, resourcePath("models/item/$id.json"))
        writes += save(cache, itemModelDefinition("$namespace:block/$id"), resourcePath("items/$id.json"))
        if (!id.endsWith("_ore")) writes += save(cache, selfDropLootTable(id), dataPath("loot_table/blocks/$id.json"))
    }

    private fun writeRecipes(cache: CachedOutput, writes: MutableList<CompletableFuture<*>>) {
        fun shaped(id: String, pattern: Array<String>) {
            val recipe = shapedRecipe(id, "equipment", pattern, "A", "aquamarine")
            if (pattern.joinToString("").contains('S')) recipe.getAsJsonObject("key").addProperty("S", "minecraft:stick")
            writes += save(cache, recipe, dataPath("recipe/$id.json"))
            writes += save(cache, recipeAdvancement(id, "equipment", "aquamarine"), dataPath("advancement/recipes/equipment/$id.json"))
        }
        shaped("aquamarine_pickaxe", arrayOf("AAA", " S ", " S ")); shaped("aquamarine_axe", arrayOf("AA ", "AS ", " S ")); shaped("aquamarine_shovel", arrayOf("A", "S", "S")); shaped("aquamarine_hoe", arrayOf("AA ", " S ", " S ")); shaped("aquamarine_sword", arrayOf("A", "A", "S"))
        shaped("aquamarine_helmet", arrayOf("AAA", "A A")); shaped("aquamarine_chestplate", arrayOf("A A", "AAA", "AAA")); shaped("aquamarine_leggings", arrayOf("AAA", "A A", "A A")); shaped("aquamarine_boots", arrayOf("A A", "A A"))
        writes += save(cache, shapedRecipe("aquamarine_block", "building", arrayOf("AAA", "AAA", "AAA"), "A", "aquamarine"), dataPath("recipe/aquamarine_block.json"))
        writes += save(cache, recipeAdvancement("aquamarine_block", "building", "aquamarine"), dataPath("advancement/recipes/building/aquamarine_block.json"))
        writes += save(cache, shapelessRecipe("aquamarine", 9, "aquamarine_block"), dataPath("recipe/aquamarine_from_aquamarine_block.json"))
        writes += save(cache, recipeAdvancement("aquamarine_from_aquamarine_block", "misc", "aquamarine_block"), dataPath("advancement/recipes/misc/aquamarine_from_aquamarine_block.json"))
        listOf("aquamarine_ore", "deepslate_aquamarine_ore").forEach { ore ->
            writes += save(cache, cookingRecipe("minecraft:smelting", ore), dataPath("recipe/aquamarine_from_smelting_$ore.json"))
            writes += save(cache, recipeAdvancement("aquamarine_from_smelting_$ore", "misc", ore), dataPath("advancement/recipes/misc/aquamarine_from_smelting_$ore.json"))
            writes += save(cache, cookingRecipe("minecraft:blasting", ore), dataPath("recipe/aquamarine_from_blasting_$ore.json"))
            writes += save(cache, recipeAdvancement("aquamarine_from_blasting_$ore", "misc", ore), dataPath("advancement/recipes/misc/aquamarine_from_blasting_$ore.json"))
        }
    }

    private fun writeWorldgen(cache: CachedOutput, writes: MutableList<CompletableFuture<*>>) {
        val feature = obj {
            addProperty("type", "minecraft:ore")
            add("config", obj {
                addProperty("discard_chance_on_air_exposure", 0.5f); addProperty("size", 4)
                add("targets", JsonArray().also { it.add(oreTarget("$namespace:aquamarine_ore", "minecraft:stone_ore_replaceables")); it.add(oreTarget("$namespace:deepslate_aquamarine_ore", "minecraft:deepslate_ore_replaceables")); it.add(oreTarget("$namespace:deepslate_aquamarine_ore", "minecraft:dripstone_block", false)) })
            })
        }
        writes += save(cache, feature, dataPath("worldgen/configured_feature/aquamarine_ore.json"))
        writes += save(cache, obj { addProperty("feature", "$namespace:aquamarine_ore"); add("placement", placement(7, -80, 80)) }, dataPath("worldgen/placed_feature/aquamarine_ore.json"))
        writes += save(cache, obj { addProperty("type", "neoforge:add_features"); add("biomes", array("#minecraft:is_ocean")); addProperty("features", "$namespace:aquamarine_ore"); addProperty("step", "underground_ores") }, dataPath("neoforge/biome_modifier/add_aquamarine_ore.json"))
    }

    private fun oreTarget(block: String, value: String, tag: Boolean = true) = obj { add("state", obj { addProperty("Name", block) }); add("target", obj { addProperty("predicate_type", if (tag) "minecraft:tag_match" else "minecraft:block_match"); addProperty(if (tag) "tag" else "block", value) }) }
    private fun placement(count: Int, min: Int, max: Int) = JsonArray().also { it.add(obj { addProperty("type", "minecraft:count"); addProperty("count", count) }); it.add(obj { addProperty("type", "minecraft:in_square") }); it.add(obj { addProperty("type", "minecraft:height_range"); add("height", obj { addProperty("type", "minecraft:trapezoid"); add("min_inclusive", obj { addProperty("above_bottom", min) }); add("max_inclusive", obj { addProperty("above_bottom", max) }) }) }); it.add(obj { addProperty("type", "minecraft:biome") }) }

    private fun oreLoot(id: String) = obj { addProperty("type", "minecraft:block"); add("pools", JsonArray().also { it.add(obj { addProperty("rolls", 1); add("entries", JsonArray().also { e -> e.add(itemLootEntry(id).apply { add("conditions", JsonArray().also { it.add(obj { addProperty("condition", "minecraft:match_tool"); add("predicate", obj { add("predicates", obj { add("minecraft:enchantments", JsonArray().also { it.add(obj { addProperty("enchantments", "minecraft:silk_touch"); add("levels", obj { addProperty("min", 1) }) }) }) }) }) }) }) }); e.add(itemLootEntry("aquamarine").apply { add("functions", JsonArray().also { it.add(obj { addProperty("function", "minecraft:apply_bonus"); addProperty("enchantment", "minecraft:fortune"); addProperty("formula", "minecraft:ore_drops") }); it.add(obj { addProperty("function", "minecraft:explosion_decay") }) }) }) }) }) }) }
    private fun equipmentAsset() = obj { add("layers", obj { add("humanoid", arrayLayer("aquamarine")); add("humanoid_baby", arrayLayer("aquamarine")); add("humanoid_leggings", arrayLayer("aquamarine")) }) }
    private fun arrayLayer(id: String) = JsonArray().also { it.add(obj { addProperty("texture", "$namespace:$id") }) }
    override fun getName() = "SupremeMC aquamarine resources"
}
