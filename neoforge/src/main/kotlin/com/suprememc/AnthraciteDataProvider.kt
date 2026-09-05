package com.suprememc

import com.google.gson.JsonArray
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class AnthraciteDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        listOf("nether_anthracite_ore", "anthracite_block").forEach { id ->
            writes += save(cache, blockState(id), resourcePath("blockstates/$id.json"))
            writes += save(cache, cubeModel(id), resourcePath("models/block/$id.json"))
            writes += save(cache, itemModelDefinition("$namespace:block/$id"), resourcePath("items/$id.json"))
        }
        writes += save(cache, selfDropLootTable("anthracite_block"), dataPath("loot_table/blocks/anthracite_block.json"))
        writes += save(cache, itemModel(), resourcePath("models/item/anthracite.json"))
        writes += save(cache, itemModelDefinition("$namespace:item/anthracite"), resourcePath("items/anthracite.json"))
        writes += save(cache, oreLoot(), dataPath("loot_table/blocks/nether_anthracite_ore.json"))
        writes += save(cache, shapedRecipe("anthracite_block", "building", arrayOf("AAA", "AAA", "AAA"), "A", "anthracite"), dataPath("recipe/anthracite_block.json"))
        writes += save(cache, shapelessRecipe("anthracite", 9, "anthracite_block"), dataPath("recipe/anthracite_from_block.json"))
        writes += save(cache, torchRecipe(), dataPath("recipe/anthracite_torch.json"))
        writes += save(cache, recipeAdvancement("anthracite_torch", "misc", "anthracite"), dataPath("advancement/recipes/misc/anthracite_torch.json"))
        writes += save(cache, oreFeature(), dataPath("worldgen/configured_feature/nether_anthracite_ore.json"))
        writes += save(cache, placedFeature(), dataPath("worldgen/placed_feature/nether_anthracite_ore.json"))
        writes += save(cache, obj { addProperty("type", "neoforge:add_features"); add("biomes", array("#minecraft:is_nether")); addProperty("features", "$namespace:nether_anthracite_ore"); addProperty("step", "underground_ores") }, dataPath("neoforge/biome_modifier/add_nether_anthracite_ore.json"))
        writes += save(cache, obj { add("values", obj { add("$namespace:anthracite", obj { addProperty("burn_time", 1600) }); add("$namespace:anthracite_block", obj { addProperty("burn_time", 16000) }) }) }, neoforgeDataPath("data_maps/item/furnace_fuels.json"))
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun blockState(id: String) = obj { add("variants", obj { add("", obj { addProperty("model", "$namespace:block/$id") }) }) }
    private fun cubeModel(id: String) = obj { addProperty("parent", "minecraft:block/cube_all"); add("textures", obj { addProperty("all", "$namespace:block/$id") }) }
    private fun itemModel() = obj { addProperty("parent", "minecraft:item/generated"); add("textures", obj { addProperty("layer0", "$namespace:item/anthracite") }) }
    private fun torchRecipe() = obj {
        addProperty("type", "minecraft:crafting_shaped")
        addProperty("category", "misc")
        add("pattern", array("A", "S"))
        add("key", obj { addProperty("A", "$namespace:anthracite"); addProperty("S", "minecraft:stick") })
        add("result", minecraftItemResult("torch", 8))
    }
    private fun oreLoot() = obj {
        addProperty("type", "minecraft:block")
        add("pools", JsonArray().also { pools -> pools.add(obj {
            addProperty("rolls", 1); add("entries", JsonArray().also { entries ->
                entries.add(itemLootEntry("nether_anthracite_ore").apply { add("conditions", JsonArray().also { it.add(silkTouchCondition()) }) })
                entries.add(itemLootEntry("anthracite").apply { add("functions", JsonArray().also { it.add(obj { addProperty("function", "minecraft:apply_bonus"); addProperty("enchantment", "minecraft:fortune"); addProperty("formula", "minecraft:ore_drops") }); it.add(obj { addProperty("function", "minecraft:explosion_decay") }) }) })
            })
        }) })
    }
    private fun silkTouchCondition() = obj { addProperty("condition", "minecraft:match_tool"); add("predicate", obj { add("predicates", obj { add("minecraft:enchantments", JsonArray().also { it.add(obj { addProperty("enchantments", "minecraft:silk_touch"); add("levels", obj { addProperty("min", 1) }) }) }) }) }) }
    private fun oreFeature() = obj { addProperty("type", "minecraft:ore"); add("config", obj { addProperty("discard_chance_on_air_exposure", 0.0f); addProperty("size", 17); add("targets", JsonArray().also { it.add(oreTarget("$namespace:nether_anthracite_ore", "minecraft:base_stone_nether")) }) }) }
    private fun oreTarget(block: String, tag: String) = obj { add("state", obj { addProperty("Name", block) }); add("target", obj { addProperty("predicate_type", "minecraft:tag_match"); addProperty("tag", tag) }) }
    private fun placedFeature() = obj { addProperty("feature", "$namespace:nether_anthracite_ore"); add("placement", JsonArray().also { it.add(obj { addProperty("type", "minecraft:count"); addProperty("count", 24) }); it.add(obj { addProperty("type", "minecraft:in_square") }); it.add(obj { addProperty("type", "minecraft:height_range"); add("height", obj { addProperty("type", "minecraft:uniform"); add("min_inclusive", obj { addProperty("above_bottom", 10) }); add("max_inclusive", obj { addProperty("below_top", 10) }) }) }); it.add(obj { addProperty("type", "minecraft:biome") }) }) }
    override fun getName() = "SupremeMC anthracite resources"
}