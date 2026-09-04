package com.suprememc

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

// Cays reuses the mushroom_fields isolated-island climate slot (see MixinOverworldBiomeBuilder), so only
// the placed feature (frequent palm cover) needs generating here; the biome definition itself is
// hand-authored under common's static resources, same convention as Florida Plains.
class CaysDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        writes += save(cache, palmPlacement(), dataPath("worldgen/placed_feature/cays_palm_trees.json"))
        writes += save(cache, springPlacement(), dataPath("worldgen/placed_feature/cays_spring_water.json"))
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    override fun getName() = "SupremeMC cays"

    // Every chunk rolls 2-4 coconut-bearing palms, restricted to sand just above the waterline, matching
    // the "relatively frequently" request while still requiring the sapling to actually survive there.
    private fun palmPlacement(): JsonObject = obj {
        addProperty("feature", "$namespace:palm_coconut")
        add("placement", JsonArray().also { placement ->
            placement.add(obj { addProperty("type", "minecraft:count"); add("count", obj { addProperty("type", "minecraft:uniform"); addProperty("min_inclusive", 3); addProperty("max_inclusive", 6) }) })
            placement.add(obj { addProperty("type", "minecraft:in_square") })
            placement.add(obj { addProperty("type", "minecraft:surface_water_depth_filter"); addProperty("max_water_depth", 0) })
            placement.add(obj { addProperty("type", "minecraft:heightmap"); addProperty("heightmap", "OCEAN_FLOOR") })
            placement.add(sandSubstrateFilter())
        })
    }

    // Vanilla spring_water rolls 25 tries per chunk; Cays is built around freshwater pools, so it rolls 200
    // against a sand/sandstone-aware spring feature.
    private fun springPlacement(): JsonObject = obj {
        addProperty("feature", "$namespace:cays_spring_water")
        add("placement", JsonArray().also { placement ->
            placement.add(obj { addProperty("type", "minecraft:count"); addProperty("count", 200) })
            placement.add(obj { addProperty("type", "minecraft:in_square") })
            placement.add(obj {
                addProperty("type", "minecraft:height_range")
                add("height", obj {
                    addProperty("type", "minecraft:uniform")
                    add("max_inclusive", obj { addProperty("absolute", 192) })
                    add("min_inclusive", obj { addProperty("above_bottom", 0) })
                })
            })
            placement.add(obj { addProperty("type", "minecraft:biome") })
        })
    }

    // Palms only take root on sand where the sapling itself would survive (mirrors PalmDataProvider's beach filter).
    private fun sandSubstrateFilter() = obj {
        addProperty("type", "minecraft:block_predicate_filter")
        add("predicate", obj {
            addProperty("type", "minecraft:all_of")
            add("predicates", JsonArray().also { predicates ->
                predicates.add(obj {
                    addProperty("type", "minecraft:matching_blocks")
                    add("blocks", array("minecraft:sand", "minecraft:red_sand"))
                    add("offset", JsonArray().also { o -> o.add(0); o.add(-1); o.add(0) })
                })
                predicates.add(obj {
                    addProperty("type", "minecraft:would_survive")
                    add("state", obj { addProperty("Name", "$namespace:palm_sapling") })
                })
            })
        })
    }
}
