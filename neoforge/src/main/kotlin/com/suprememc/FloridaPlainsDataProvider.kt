package com.suprememc

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

// Florida Plains reuses the swamp/mangrove_swamp overworld climate slot (see MixinOverworldBiomeBuilder),
// so only the placed feature (mixed tree cover) and the witch-hut biome tag need generating here; the
// biome definition and the mixed-tree configured feature are hand-authored under common's static resources.
class FloridaPlainsDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        writes += save(cache, treesPlacement(), dataPath("worldgen/placed_feature/florida_plains_trees.json"))
        // random_selector entries in the mixed-tree configured feature bind to placed features in 26.2,
        // so each tree needs a placement-free ("bare") wrapper to resolve.
        writes += save(cache, barePlacedFeature("$namespace:palm_coconut"), dataPath("worldgen/placed_feature/palm_coconut_bare.json"))
        writes += save(cache, barePlacedFeature("minecraft:swamp_oak"), dataPath("worldgen/placed_feature/swamp_oak_bare.json"))
        // Additive merge: keeps vanilla's existing "minecraft:swamp" entry alongside ours.
        writes += save(cache, valuesTag("$namespace:florida_plains"), minecraftDataPath("tags/worldgen/biome/has_structure/swamp_hut.json"))
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    // Wraps a configured feature with no placement modifiers, for use inside random_selector entries.
    private fun barePlacedFeature(configuredFeature: String): JsonObject = obj {
        addProperty("feature", configuredFeature)
        add("placement", JsonArray())
    }

    override fun getName() = "SupremeMC florida plains"

    // Mirrors vanilla's trees_swamp.json placement (2-3 trees per chunk, shallow water tolerant).
    private fun treesPlacement(): JsonObject = obj {
        addProperty("feature", "$namespace:florida_plains_trees")
        add("placement", JsonArray().also { placement ->
            placement.add(obj {
                addProperty("type", "minecraft:count")
                add("count", obj {
                    addProperty("type", "minecraft:weighted_list")
                    add("distribution", JsonArray().also { distribution ->
                        distribution.add(obj { addProperty("data", 2); addProperty("weight", 9) })
                        distribution.add(obj { addProperty("data", 3); addProperty("weight", 1) })
                    })
                })
            })
            placement.add(obj { addProperty("type", "minecraft:in_square") })
            placement.add(obj { addProperty("type", "minecraft:surface_water_depth_filter"); addProperty("max_water_depth", 2) })
            placement.add(obj { addProperty("type", "minecraft:heightmap"); addProperty("heightmap", "OCEAN_FLOOR") })
            // Without this, a chunk's second/third tree can land on the first tree's log/leaves,
            // since OCEAN_FLOOR only ignores air/leaves and doesn't require solid ground below.
            placement.add(groundSubstrateFilter())
            placement.add(obj { addProperty("type", "minecraft:biome") })
        })
    }

    // Restricts the mixed tree cover to dirt, grass, or sand so trees never stack on each other.
    private fun groundSubstrateFilter() = obj {
        addProperty("type", "minecraft:block_predicate_filter")
        add("predicate", obj {
            addProperty("type", "minecraft:matching_blocks")
            add("blocks", array("minecraft:dirt", "minecraft:grass_block", "minecraft:sand"))
            add("offset", JsonArray().also { o -> o.add(0); o.add(-1); o.add(0) })
        })
    }
}
