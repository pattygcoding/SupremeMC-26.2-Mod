package com.suprememc

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

// Ice Caves reuses the dripstone_caves underground climate slot (see MixinOverworldBiomeBuilder), so only
// the placed features (packed-ice clusters + icicles) need generating here; the biome definition and the
// re-skinned configured features are hand-authored under common's static resources.
class IceCavesDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        writes += save(cache, iceClusterPlacement(), dataPath("worldgen/placed_feature/ice_cluster.json"))
        writes += save(cache, pointedIciclePlacement(), dataPath("worldgen/placed_feature/pointed_icicle.json"))
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    // Mirrors vanilla's dripstone_cluster.json placement.
    private fun iceClusterPlacement(): JsonObject = obj {
        addProperty("feature", "$namespace:ice_cluster")
        add("placement", JsonArray().also { placement ->
            placement.add(obj {
                addProperty("type", "minecraft:count")
                add("count", obj { addProperty("type", "minecraft:uniform"); addProperty("max_inclusive", 96); addProperty("min_inclusive", 48) })
            })
            placement.add(obj { addProperty("type", "minecraft:in_square") })
            placement.add(heightRange())
            placement.add(obj { addProperty("type", "minecraft:biome") })
        })
    }

    // Mirrors vanilla's pointed_dripstone.json placement.
    private fun pointedIciclePlacement(): JsonObject = obj {
        addProperty("feature", "$namespace:pointed_icicle")
        add("placement", JsonArray().also { placement ->
            placement.add(obj {
                addProperty("type", "minecraft:count")
                add("count", obj { addProperty("type", "minecraft:uniform"); addProperty("max_inclusive", 256); addProperty("min_inclusive", 192) })
            })
            placement.add(obj { addProperty("type", "minecraft:in_square") })
            placement.add(heightRange())
            placement.add(obj {
                addProperty("type", "minecraft:count")
                add("count", obj { addProperty("type", "minecraft:uniform"); addProperty("max_inclusive", 5); addProperty("min_inclusive", 1) })
            })
            placement.add(obj {
                addProperty("type", "minecraft:random_offset")
                add("xz_spread", obj {
                    addProperty("type", "minecraft:clamped_normal")
                    addProperty("deviation", 3.0)
                    addProperty("max_inclusive", 10)
                    addProperty("mean", 0.0)
                    addProperty("min_inclusive", -10)
                })
                add("y_spread", obj {
                    addProperty("type", "minecraft:clamped_normal")
                    addProperty("deviation", 0.6)
                    addProperty("max_inclusive", 2)
                    addProperty("mean", 0.0)
                    addProperty("min_inclusive", -2)
                })
            })
            placement.add(obj { addProperty("type", "minecraft:biome") })
        })
    }

    private fun heightRange() = obj {
        addProperty("type", "minecraft:height_range")
        add("height", obj {
            addProperty("type", "minecraft:uniform")
            add("max_inclusive", obj { addProperty("absolute", 256) })
            add("min_inclusive", obj { addProperty("above_bottom", 0) })
        })
    }

    override fun getName() = "SupremeMC ice caves"
}
