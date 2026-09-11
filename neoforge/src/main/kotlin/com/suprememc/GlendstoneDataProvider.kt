package com.suprememc

import com.google.gson.JsonArray
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class GlendstoneDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val id = "glendstone"
        val writes = listOf(
            save(cache, simpleBlockState(id), resourcePath("blockstates/$id.json")),
            save(cache, cubeModel(id), resourcePath("models/block/$id.json")),
            save(cache, itemBlockModel(id), resourcePath("models/item/$id.json")),
            save(cache, itemModelDefinition("$namespace:block/$id"), resourcePath("items/$id.json")),
            save(cache, glendstoneLootTable(), dataPath("loot_table/blocks/$id.json")),
            save(cache, shapedRecipe(id, "building", arrayOf("##", "##"), "#", "glendstone_dust"), dataPath("recipe/$id.json")),
            save(cache, recipeAdvancement(id, "building", "glendstone_dust"), dataPath("advancement/recipes/building/$id.json")),
            save(cache, glendstoneClusterFeature(), dataPath("worldgen/configured_feature/glendstone_cluster.json")),
            save(cache, placedFeature("glendstone_cluster", glowstonePlacement()), dataPath("worldgen/placed_feature/glendstone.json")),
            save(cache, placedFeature("glendstone_cluster", glowstoneExtraPlacement()), dataPath("worldgen/placed_feature/glendstone_extra.json")),
            save(cache, addGlendstoneBiomeModifier("glendstone"), dataPath("neoforge/biome_modifier/add_glendstone.json")),
            save(cache, addGlendstoneBiomeModifier("glendstone_extra"), dataPath("neoforge/biome_modifier/add_glendstone_extra.json")),
        )
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun glendstoneClusterFeature() = obj {
        addProperty("type", "$namespace:glendstone_cluster")
        add("config", obj {})
    }

    private fun glendstoneLootTable() = obj {
        addProperty("type", "minecraft:block")
        add("pools", JsonArray().also { pools ->
            pools.add(obj {
                addProperty("rolls", 1)
                add("entries", JsonArray().also { entries ->
                    entries.add(obj {
                        addProperty("type", "minecraft:alternatives")
                        add("children", JsonArray().also { children ->
                            children.add(itemLootEntry("glendstone").also { entry -> entry.add("conditions", JsonArray().also { it.add(silkTouchCondition()) }) })
                            children.add(itemLootEntry("glendstone_dust").also { entry ->
                                entry.add("functions", JsonArray().also { functions ->
                                    functions.add(obj {
                                        addProperty("function", "minecraft:set_count")
                                        add("count", obj {
                                            addProperty("type", "minecraft:uniform")
                                            addProperty("min", 2.0)
                                            addProperty("max", 4.0)
                                        })
                                    })
                                    functions.add(obj {
                                        addProperty("function", "minecraft:apply_bonus")
                                        addProperty("enchantment", "minecraft:fortune")
                                        addProperty("formula", "minecraft:uniform_bonus_count")
                                        add("parameters", obj { addProperty("bonusMultiplier", 1) })
                                    })
                                    functions.add(obj {
                                        addProperty("function", "minecraft:limit_count")
                                        add("limit", obj { addProperty("min", 1.0); addProperty("max", 4.0) })
                                    })
                                    functions.add(obj { addProperty("function", "minecraft:explosion_decay") })
                                })
                            })
                        })
                    })
                })
            })
        })
        addProperty("random_sequence", "$namespace:blocks/glendstone")
    }

    private fun silkTouchCondition() = obj {
        addProperty("condition", "minecraft:match_tool")
        add("predicate", obj {
            add("predicates", obj {
                add("minecraft:enchantments", JsonArray().also {
                    it.add(obj {
                        addProperty("enchantments", "minecraft:silk_touch")
                        add("levels", obj { addProperty("min", 1) })
                    })
                })
            })
        })
    }

    private fun placedFeature(feature: String, placement: JsonArray) = obj {
        addProperty("feature", "$namespace:$feature")
        add("placement", placement)
    }

    private fun glowstonePlacement() = JsonArray().also { placement ->
        placement.add(obj { addProperty("type", "minecraft:count"); addProperty("count", 10) })
        placement.add(obj { addProperty("type", "minecraft:in_square") })
        placement.add(heightRange("above_bottom", 0, "below_top", 0))
        placement.add(obj { addProperty("type", "minecraft:biome") })
    }

    private fun glowstoneExtraPlacement() = JsonArray().also { placement ->
        placement.add(obj {
            addProperty("type", "minecraft:count")
            add("count", obj {
                addProperty("type", "minecraft:biased_to_bottom")
                addProperty("min_inclusive", 0)
                addProperty("max_inclusive", 9)
            })
        })
        placement.add(obj { addProperty("type", "minecraft:in_square") })
        placement.add(heightRange("above_bottom", 4, "below_top", 4))
        placement.add(obj { addProperty("type", "minecraft:biome") })
    }

    private fun heightRange(minAnchor: String, minValue: Int, maxAnchor: String, maxValue: Int) = obj {
        addProperty("type", "minecraft:height_range")
        add("height", obj {
            addProperty("type", "minecraft:uniform")
            add("min_inclusive", obj { addProperty(minAnchor, minValue) })
            add("max_inclusive", obj { addProperty(maxAnchor, maxValue) })
        })
    }

    private fun addGlendstoneBiomeModifier(feature: String) = obj {
        addProperty("type", "neoforge:add_features")
        add("biomes", array(
            "#minecraft:is_end",
            "$namespace:lavender_barrens",
            "$namespace:lavender_midlands",
            "$namespace:lavender_highlands",
        ))
        addProperty("features", "$namespace:$feature")
        addProperty("step", "underground_decoration")
    }

    private fun simpleBlockState(id: String) = obj {
        add("variants", obj { add("", obj { addProperty("model", "$namespace:block/$id") }) })
    }

    private fun cubeModel(id: String) = obj {
        addProperty("parent", "minecraft:block/cube_all")
        add("textures", obj { addProperty("all", "$namespace:block/$id") })
    }

    private fun itemBlockModel(id: String) = obj {
        addProperty("parent", "$namespace:block/$id")
    }

    override fun getName() = "Glendstone"
}
