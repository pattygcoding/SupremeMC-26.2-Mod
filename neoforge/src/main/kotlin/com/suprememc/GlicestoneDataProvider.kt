package com.suprememc

import com.google.gson.JsonArray
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class GlicestoneDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val id = "glicestone"
        val writes = listOf(
            save(cache, simpleBlockState(id), resourcePath("blockstates/$id.json")),
            save(cache, cubeModel(id), resourcePath("models/block/$id.json")),
            save(cache, itemBlockModel(id), resourcePath("models/item/$id.json")),
            save(cache, itemModelDefinition("$namespace:block/$id"), resourcePath("items/$id.json")),
            save(cache, glicestoneLootTable(), dataPath("loot_table/blocks/$id.json")),
            save(cache, shapedRecipe(id, "building", arrayOf("##", "##"), "#", "glicestone_dust"), dataPath("recipe/$id.json")),
            save(cache, recipeAdvancement(id, "building", "glicestone_dust"), dataPath("advancement/recipes/building/$id.json")),
            save(cache, glicestoneClusterFeature(), dataPath("worldgen/configured_feature/glicestone_cluster.json")),
            save(cache, placedFeature("glicestone_cluster", glowstonePlacement()), dataPath("worldgen/placed_feature/glicestone.json")),
            save(cache, placedFeature("glicestone_cluster", glowstoneExtraPlacement()), dataPath("worldgen/placed_feature/glicestone_extra.json")),
            save(cache, addGlicestoneBiomeModifier("glicestone"), dataPath("neoforge/biome_modifier/add_glicestone.json")),
            save(cache, addGlicestoneBiomeModifier("glicestone_extra"), dataPath("neoforge/biome_modifier/add_glicestone_extra.json")),
        )
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun glicestoneClusterFeature() = obj {
        addProperty("type", "$namespace:glicestone_cluster")
        add("config", obj {})
    }

    private fun glicestoneLootTable() = obj {
        addProperty("type", "minecraft:block")
        add("pools", JsonArray().also { pools ->
            pools.add(obj {
                addProperty("rolls", 1)
                add("entries", JsonArray().also { entries ->
                    entries.add(obj {
                        addProperty("type", "minecraft:alternatives")
                        add("children", JsonArray().also { children ->
                            children.add(itemLootEntry("glicestone").also { entry -> entry.add("conditions", JsonArray().also { it.add(silkTouchCondition()) }) })
                            children.add(itemLootEntry("glicestone_dust").also { entry ->
                                entry.add("functions", JsonArray().also { functions ->
                                    functions.add(obj { addProperty("function", "minecraft:set_count"); add("count", obj { addProperty("type", "minecraft:uniform"); addProperty("min", 2.0); addProperty("max", 4.0) }) })
                                    functions.add(obj { addProperty("function", "minecraft:apply_bonus"); addProperty("enchantment", "minecraft:fortune"); addProperty("formula", "minecraft:uniform_bonus_count"); add("parameters", obj { addProperty("bonusMultiplier", 1) }) })
                                    functions.add(obj { addProperty("function", "minecraft:limit_count"); add("limit", obj { addProperty("min", 1.0); addProperty("max", 4.0) }) })
                                    functions.add(obj { addProperty("function", "minecraft:explosion_decay") })
                                })
                            })
                        })
                    })
                })
            })
        })
        addProperty("random_sequence", "$namespace:blocks/glicestone")
    }

    private fun silkTouchCondition() = obj {
        addProperty("condition", "minecraft:match_tool")
        add("predicate", obj { add("predicates", obj { add("minecraft:enchantments", JsonArray().also { it.add(obj { addProperty("enchantments", "minecraft:silk_touch"); add("levels", obj { addProperty("min", 1) }) }) }) }) })
    }

    private fun placedFeature(feature: String, placement: JsonArray) = obj { addProperty("feature", "$namespace:$feature"); add("placement", placement) }

    private fun glowstonePlacement() = JsonArray().also { placement ->
        placement.add(obj { addProperty("type", "minecraft:count"); addProperty("count", 10) })
        placement.add(obj { addProperty("type", "minecraft:in_square") })
        placement.add(heightRange("above_bottom", 0, "below_top", 0))
        placement.add(obj { addProperty("type", "minecraft:biome") })
    }

    private fun glowstoneExtraPlacement() = JsonArray().also { placement ->
        placement.add(obj { addProperty("type", "minecraft:count"); add("count", obj { addProperty("type", "minecraft:biased_to_bottom"); addProperty("min_inclusive", 0); addProperty("max_inclusive", 9) }) })
        placement.add(obj { addProperty("type", "minecraft:in_square") })
        placement.add(heightRange("above_bottom", 4, "below_top", 4))
        placement.add(obj { addProperty("type", "minecraft:biome") })
    }

    private fun heightRange(minAnchor: String, minValue: Int, maxAnchor: String, maxValue: Int) = obj {
        addProperty("type", "minecraft:height_range")
        add("height", obj { addProperty("type", "minecraft:uniform"); add("min_inclusive", obj { addProperty(minAnchor, minValue) }); add("max_inclusive", obj { addProperty(maxAnchor, maxValue) }) })
    }

    private fun addGlicestoneBiomeModifier(feature: String) = obj {
        addProperty("type", "neoforge:add_features")
        add("biomes", array("#minecraft:is_end", "$namespace:lavender_barrens", "$namespace:lavender_midlands", "$namespace:lavender_highlands", "$namespace:icether_wastes"))
        addProperty("features", "$namespace:$feature")
        addProperty("step", "underground_decoration")
    }

    private fun simpleBlockState(id: String) = obj { add("variants", obj { add("", obj { addProperty("model", "$namespace:block/$id") }) }) }
    private fun cubeModel(id: String) = obj { addProperty("parent", "minecraft:block/cube_all"); add("textures", obj { addProperty("all", "$namespace:block/$id") }) }
    private fun itemBlockModel(id: String) = obj { addProperty("parent", "$namespace:block/$id") }

    override fun getName() = "Glicestone"
}