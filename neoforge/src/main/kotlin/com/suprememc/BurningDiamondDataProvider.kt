package com.suprememc

import com.google.gson.JsonArray
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class BurningDiamondDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    private val blocks = arrayOf("burning_diamond_ore", "burning_diamond_block")
    private val items = arrayOf(
        "burning_diamond", "burning_diamond_pickaxe", "burning_diamond_axe", "burning_diamond_shovel",
        "burning_diamond_hoe", "burning_diamond_sword", "burning_diamond_helmet", "burning_diamond_chestplate",
            "burning_diamond_leggings", "burning_diamond_boots", "burning_netherite_pickaxe",
        "burning_netherite_axe", "burning_netherite_shovel", "burning_netherite_hoe", "burning_netherite_sword",
        "burning_netherite_helmet", "burning_netherite_chestplate", "burning_netherite_leggings", "burning_netherite_boots"
    )

    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        blocks.forEach { writeBlock(cache, writes, it) }
        modelFiles(cache, writes, items)
        writes += save(cache, equipmentAsset(), resourcePath("equipment/burning_diamond.json"))
        writes += save(cache, netheriteEquipmentAsset(), resourcePath("equipment/burning_netherite.json"))
        writes += save(cache, oreLoot(), dataPath("loot_table/blocks/burning_diamond_ore.json"))
        writes += save(cache, oreFeature(), dataPath("worldgen/configured_feature/burning_diamond_ore.json"))
        writes += save(cache, placedFeature(), dataPath("worldgen/placed_feature/burning_diamond_ore.json"))
        writes += save(cache, obj { addProperty("type", "neoforge:add_features"); add("biomes", array("#minecraft:is_nether")); addProperty("features", "$namespace:burning_diamond_ore"); addProperty("step", "underground_ores") }, dataPath("neoforge/biome_modifier/add_burning_diamond_ore.json"))
        writeRecipes(cache, writes)
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun writeRecipes(cache: CachedOutput, writes: MutableList<CompletableFuture<*>>) {
        writes += save(cache, shapedRecipe("burning_diamond_block", "building", arrayOf("AAA", "AAA", "AAA"), "A", "burning_diamond"), dataPath("recipe/burning_diamond_block.json"))
        writes += save(cache, recipeAdvancement("burning_diamond_block", "building", "burning_diamond"), dataPath("advancement/recipes/building/burning_diamond_block.json"))
        writes += save(cache, shapelessRecipe("burning_diamond", 9, "burning_diamond_block"), dataPath("recipe/burning_diamond_from_block.json"))
        writes += save(cache, recipeAdvancement("burning_diamond_from_block", "misc", "burning_diamond_block"), dataPath("advancement/recipes/misc/burning_diamond_from_block.json"))
        fun shaped(id: String, pattern: Array<String>, material: String = "burning_diamond") {
            val recipe = shapedRecipe(id, "equipment", pattern, "A", material)
            if (pattern.joinToString("").contains('S')) recipe.getAsJsonObject("key").addProperty("S", "minecraft:stick")
            writes += save(cache, recipe, dataPath("recipe/$id.json"))
            writes += save(cache, recipeAdvancement(id, "equipment", "burning_diamond"), dataPath("advancement/recipes/equipment/$id.json"))
        }
        shaped("burning_diamond_pickaxe", arrayOf("AAA", " S ", " S "))
        shaped("burning_diamond_axe", arrayOf("AA ", "AS ", " S "))
        shaped("burning_diamond_shovel", arrayOf("A", "S", "S"))
        shaped("burning_diamond_hoe", arrayOf("AA ", " S ", " S "))
        shaped("burning_diamond_sword", arrayOf("A", "A", "S"))
        shaped("burning_diamond_helmet", arrayOf("AAA", "A A"))
        shaped("burning_diamond_chestplate", arrayOf("A A", "AAA", "AAA"))
        shaped("burning_diamond_leggings", arrayOf("AAA", "A A", "A A"))
        shaped("burning_diamond_boots", arrayOf("A A", "A A"))
        listOf("pickaxe", "axe", "shovel", "hoe", "sword", "helmet", "chestplate", "leggings", "boots").forEach { id ->
            val recipeId = "burning_netherite_$id"
            writes += save(cache, smithing(recipeId, "burning_diamond_$id"), dataPath("recipe/$recipeId.json"))
            writes += save(cache, recipeAdvancement(recipeId, "misc", "burning_diamond_$id"), dataPath("advancement/recipes/misc/$recipeId.json"))
        }
    }

    private fun smithing(result: String, base: String) = obj {
        addProperty("type", "minecraft:smithing_transform")
        addProperty("template", minecraftIngredient("netherite_upgrade_smithing_template"))
        addProperty("base", ingredient(base))
        addProperty("addition", minecraftIngredient("netherite_ingot"))
        add("result", itemResult(result))
    }

    private fun writeBlock(cache: CachedOutput, writes: MutableList<CompletableFuture<*>>, id: String) {
        writes += save(cache, obj { add("variants", obj { add("", obj { addProperty("model", "$namespace:block/$id") }) }) }, resourcePath("blockstates/$id.json"))
        writes += save(cache, obj {
            addProperty("parent", "minecraft:block/cube_all")
            add("textures", obj { addProperty("all", "$namespace:block/$id") })
        }, resourcePath("models/block/$id.json"))
        writes += save(cache, obj { addProperty("parent", "$namespace:block/$id") }, resourcePath("models/item/$id.json"))
        writes += save(cache, itemModelDefinition("$namespace:block/$id"), resourcePath("items/$id.json"))
        if (id == "burning_diamond_block") writes += save(cache, selfDropLootTable(id), dataPath("loot_table/blocks/$id.json"))
    }

    private fun oreLoot() = obj {
        addProperty("type", "minecraft:block")
        add("pools", JsonArray().also { pools -> pools.add(obj {
            addProperty("rolls", 1)
            add("entries", JsonArray().also { entries ->
                entries.add(itemLootEntry("burning_diamond_ore").apply { add("conditions", JsonArray().also { it.add(silkTouchCondition()) }) })
                entries.add(itemLootEntry("burning_diamond").apply { add("functions", JsonArray().also { it.add(obj { addProperty("function", "minecraft:apply_bonus"); addProperty("enchantment", "minecraft:fortune"); addProperty("formula", "minecraft:ore_drops") }); it.add(obj { addProperty("function", "minecraft:explosion_decay") }) }) })
            })
        }) })
    }

    private fun silkTouchCondition() = obj { addProperty("condition", "minecraft:match_tool"); add("predicate", obj { add("predicates", obj { add("minecraft:enchantments", JsonArray().also { it.add(obj { addProperty("enchantments", "minecraft:silk_touch"); add("levels", obj { addProperty("min", 1) }) }) }) }) }) }
    private fun oreFeature() = obj { addProperty("type", "minecraft:ore"); add("config", obj { addProperty("discard_chance_on_air_exposure", 0.5f); addProperty("size", 8); add("targets", JsonArray().also { it.add(oreTarget("$namespace:burning_diamond_ore", "minecraft:base_stone_nether")) }) }) }
    private fun oreTarget(block: String, tag: String) = obj { add("state", obj { addProperty("Name", block) }); add("target", obj { addProperty("predicate_type", "minecraft:tag_match"); addProperty("tag", tag) }) }
    private fun placedFeature() = obj { addProperty("feature", "$namespace:burning_diamond_ore"); add("placement", JsonArray().also { it.add(obj { addProperty("type", "minecraft:count"); addProperty("count", 7) }); it.add(obj { addProperty("type", "minecraft:in_square") }); it.add(obj { addProperty("type", "minecraft:height_range"); add("height", obj { addProperty("type", "minecraft:trapezoid"); add("min_inclusive", obj { addProperty("above_bottom", 0) }); add("max_inclusive", obj { addProperty("above_bottom", 15) }) }) }); it.add(obj { addProperty("type", "minecraft:biome") }) }) }

    private fun equipmentAsset() = obj {
        add("layers", obj {
            add("humanoid", arrayLayer("burning_diamond"))
            add("humanoid_baby", arrayLayer("burning_diamond"))
            add("humanoid_leggings", arrayLayer("burning_diamond"))
        })
    }

    private fun netheriteEquipmentAsset() = obj {
        add("layers", obj {
            add("humanoid", arrayLayer("burning_netherite"))
            add("humanoid_baby", arrayLayer("burning_netherite"))
            add("humanoid_leggings", arrayLayer("burning_netherite"))
        })
    }

    private fun arrayLayer(id: String) = JsonArray().also { it.add(obj { addProperty("texture", "$namespace:$id") }) }

    override fun getName() = "SupremeMC burning diamond resources"
}