package com.suprememc

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class BeachGrassDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        writes += save(cache, shortBlockState("beach_grass"), resourcePath("blockstates/beach_grass.json"))
        writes += save(cache, tallBlockState(), resourcePath("blockstates/tall_beach_grass.json"))
        writes += save(cache, crossModel("beach_grass"), resourcePath("models/block/beach_grass.json"))
        writes += save(cache, crossModel("beach_grass_tall_bottom"), resourcePath("models/block/beach_grass_tall_bottom.json"))
        writes += save(cache, crossModel("beach_grass_tall_top"), resourcePath("models/block/beach_grass_tall_top.json"))
        writes += save(cache, flatItemModel("beach_grass"), resourcePath("models/item/beach_grass.json"))
        writes += save(cache, flatItemModel("beach_grass_tall_top"), resourcePath("models/item/tall_beach_grass.json"))
        writes += save(cache, itemModelDefinition("$namespace:item/beach_grass", grassTints()), resourcePath("items/beach_grass.json"))
        writes += save(cache, itemModelDefinition("$namespace:item/tall_beach_grass", grassTints()), resourcePath("items/tall_beach_grass.json"))
        writes += save(cache, grassLoot("beach_grass", 1), dataPath("loot_table/blocks/beach_grass.json"))
        writes += save(cache, grassLoot("tall_beach_grass", 2), dataPath("loot_table/blocks/tall_beach_grass.json"))
        writes += save(cache, simpleBlockFeature("beach_grass", "beach_grass"), dataPath("worldgen/configured_feature/beach_grass.json"))
        writes += save(cache, simpleBlockFeature("tall_beach_grass", "tall_beach_grass", "lower"), dataPath("worldgen/configured_feature/tall_beach_grass.json"))
        writes += save(cache, grassPlacement("beach_grass", "WORLD_SURFACE_WG"), dataPath("worldgen/placed_feature/beach_grass.json"))
        writes += save(cache, tallGrassPlacement(), dataPath("worldgen/placed_feature/tall_beach_grass.json"))
        writes += save(cache, beachBiomeModifier("beach_grass"), dataPath("neoforge/biome_modifier/add_beach_grass.json"))
        writes += save(cache, beachBiomeModifier("tall_beach_grass"), dataPath("neoforge/biome_modifier/add_tall_beach_grass.json"))
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun simpleBlockFeature(id: String, block: String, half: String? = null) = obj {
        addProperty("type", "minecraft:simple_block")
        add("config", obj {
            add("to_place", obj {
                addProperty("type", "minecraft:simple_state_provider")
                add("state", obj {
                    addProperty("Name", "$namespace:$block")
                    if (half != null) add("Properties", obj { addProperty("half", half) })
                })
            })
        })
    }

    private fun grassPlacement(feature: String, heightmap: String) = obj {
        addProperty("feature", "$namespace:$feature")
        add("placement", JsonArray().also { placement ->
            placement.add(obj { addProperty("type", "minecraft:noise_threshold_count"); addProperty("above_noise", 10); addProperty("below_noise", 5); addProperty("noise_level", -0.8) })
            placement.add(obj { addProperty("type", "minecraft:in_square") })
            placement.add(obj { addProperty("type", "minecraft:heightmap"); addProperty("heightmap", heightmap) })
            placement.add(obj { addProperty("type", "minecraft:biome") })
            placement.add(obj { addProperty("type", "minecraft:count"); addProperty("count", 32) })
            placement.add(randomOffset(7, 3))
            placement.add(sandSurfaceFilter())
        })
    }

    private fun tallGrassPlacement() = obj {
        addProperty("feature", "$namespace:tall_beach_grass")
        add("placement", JsonArray().also { placement ->
            placement.add(obj { addProperty("type", "minecraft:rarity_filter"); addProperty("chance", 5) })
            placement.add(obj { addProperty("type", "minecraft:in_square") })
            placement.add(obj { addProperty("type", "minecraft:heightmap"); addProperty("heightmap", "MOTION_BLOCKING") })
            placement.add(obj { addProperty("type", "minecraft:biome") })
            placement.add(obj { addProperty("type", "minecraft:count"); addProperty("count", 96) })
            placement.add(randomOffset(7, 3))
            placement.add(sandSurfaceFilter())
        })
    }

    private fun randomOffset(xzSpread: Int, ySpread: Int) = obj {
        addProperty("type", "minecraft:random_offset")
        add("xz_spread", trapezoid(xzSpread))
        add("y_spread", trapezoid(ySpread))
    }

    private fun trapezoid(spread: Int) = obj {
        addProperty("type", "minecraft:trapezoid")
        addProperty("max", spread)
        addProperty("min", -spread)
        addProperty("plateau", 0)
    }

    private fun sandSurfaceFilter() = obj {
        addProperty("type", "minecraft:block_predicate_filter")
        add("predicate", obj {
            addProperty("type", "minecraft:all_of")
            add("predicates", JsonArray().also { predicates ->
                predicates.add(obj {
                    addProperty("type", "minecraft:matching_blocks")
                    add("blocks", array("minecraft:sand", "minecraft:red_sand"))
                    add("offset", JsonArray().also { offset -> offset.add(0); offset.add(-1); offset.add(0) })
                })
                predicates.add(obj { addProperty("type", "minecraft:matching_block_tag"); addProperty("tag", "minecraft:air") })
            })
        })
    }

    private fun beachBiomeModifier(feature: String) = obj {
        addProperty("type", "neoforge:add_features")
        add("biomes", JsonArray().also { biomes ->
            biomes.add("#minecraft:is_beach")
            biomes.add("$namespace:cays")
        })
        addProperty("features", "$namespace:$feature")
        addProperty("step", "vegetal_decoration")
    }

    private fun shortBlockState(id: String) = obj {
        add("variants", obj {
            add("", obj { addProperty("model", "$namespace:block/$id") })
        })
    }

    private fun tallBlockState() = obj {
        add("variants", obj {
            add("half=lower", obj { addProperty("model", "$namespace:block/beach_grass_tall_bottom") })
            add("half=upper", obj { addProperty("model", "$namespace:block/beach_grass_tall_top") })
        })
    }

    private fun crossModel(texture: String) = obj {
        addProperty("parent", "minecraft:block/tinted_cross")
        add("textures", obj { addProperty("cross", "$namespace:block/$texture") })
    }

    private fun flatItemModel(texture: String) = obj {
        addProperty("parent", "minecraft:item/generated")
        add("textures", obj { addProperty("layer0", "$namespace:block/$texture") })
    }

    private fun grassTints() = JsonArray().also {
        it.add(obj {
            addProperty("type", "minecraft:grass")
            addProperty("downfall", 1.0)
            addProperty("temperature", 0.5)
        })
    }

    private fun grassLoot(blockId: String, shearedCount: Int) = obj {
        addProperty("type", "minecraft:block")
        add("pools", JsonArray().also { pools ->
            pools.add(obj {
                addProperty("rolls", 1.0)
                add("entries", JsonArray().also { entries ->
                    entries.add(obj {
                        addProperty("type", "minecraft:alternatives")
                        add("children", JsonArray().also { children ->
                            children.add(obj {
                                addProperty("type", "minecraft:item")
                                add("conditions", JsonArray().also { it.add(shearsCondition()) })
                                if (shearedCount > 1) {
                                    add("functions", JsonArray().also { it.add(obj { addProperty("function", "minecraft:set_count"); addProperty("count", shearedCount) }) })
                                }
                                addProperty("name", "$namespace:beach_grass")
                            })
                            children.add(obj {
                                addProperty("type", "minecraft:item")
                                add("conditions", JsonArray().also {
                                    if (shearedCount > 1) it.add(obj { addProperty("condition", "minecraft:survives_explosion") })
                                    it.add(obj { addProperty("condition", "minecraft:random_chance"); addProperty("chance", 0.125) })
                                })
                                addProperty("name", "minecraft:wheat_seeds")
                            })
                        })
                    })
                })
            })
        })
        addProperty("random_sequence", "$namespace:blocks/$blockId")
    }

    private fun shearsCondition() = obj {
        addProperty("condition", "minecraft:match_tool")
        add("predicate", obj { addProperty("items", "minecraft:shears") })
    }

    override fun getName() = "SupremeMC beach grass"
}
