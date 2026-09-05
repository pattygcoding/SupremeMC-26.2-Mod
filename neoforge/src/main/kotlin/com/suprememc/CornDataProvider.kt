package com.suprememc

import com.google.gson.JsonArray
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class CornDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        listOf("corn_stalk", "corn_stalk_plant").forEach { id ->
            writes += save(cache, blockState(id), resourcePath("blockstates/$id.json"))
            writes += save(cache, model("minecraft:block/cross", "cross" to "$namespace:block/$id"), resourcePath("models/block/$id.json"))
            writes += save(cache, cornLoot(), dataPath("loot_table/blocks/$id.json"))
        }
        listOf("corn_stalk", "corn_stalk_plant").forEach { id ->
            writes += save(cache, itemModelDefinition("$namespace:block/$id"), resourcePath("items/$id.json"))
        }
        writes += save(cache, cornPatch(), dataPath("worldgen/configured_feature/corn_patches.json"))
        writes += save(cache, cornPlacement(), dataPath("worldgen/placed_feature/corn_patches.json"))
        writes += save(cache, plainsBiomeModifier(), dataPath("neoforge/biome_modifier/add_corn_patches.json"))
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun blockState(id: String) = obj {
        add("variants", obj { add("", obj { addProperty("model", "$namespace:block/$id") }) })
    }

    private fun model(parent: String, vararg textures: Pair<String, String>) = obj {
        addProperty("parent", parent)
        add("textures", obj { textures.forEach { (key, value) -> addProperty(key, value) } })
    }

    private fun cornLoot() = obj {
        addProperty("type", "minecraft:block")
        add("pools", JsonArray().also { pools -> pools.add(obj {
            addProperty("rolls", 1)
            add("entries", JsonArray().also { entries -> entries.add(itemLootEntry("corn")) })
        }) })
        addProperty("random_sequence", "$namespace:blocks/corn_stalk")
    }

    private fun cornPatch() = obj {
        addProperty("type", "$namespace:corn_patch")
        add("config", obj {})
    }

    private fun cornPlacement() = obj {
        addProperty("feature", "$namespace:corn_patches")
        add("placement", JsonArray().also { placement ->
            placement.add(obj { addProperty("type", "minecraft:rarity_filter"); addProperty("chance", 5) })
            placement.add(obj { addProperty("type", "minecraft:in_square") })
            placement.add(obj { addProperty("type", "minecraft:heightmap"); addProperty("heightmap", "MOTION_BLOCKING") })
            placement.add(obj { addProperty("type", "minecraft:biome") })
            placement.add(obj { addProperty("type", "minecraft:count"); addProperty("count", 24) })
            placement.add(obj {
                addProperty("type", "minecraft:random_offset")
                add("xz_spread", trapezoid(5))
                add("y_spread", trapezoid(1))
            })
            placement.add(obj {
                addProperty("type", "minecraft:block_predicate_filter")
                add("predicate", obj { addProperty("type", "minecraft:matching_block_tag"); addProperty("tag", "minecraft:air") })
            })
        })
    }

    private fun trapezoid(spread: Int) = obj {
        addProperty("type", "minecraft:trapezoid")
        addProperty("max", spread)
        addProperty("min", -spread)
        addProperty("plateau", 0)
    }

    private fun plainsBiomeModifier() = obj {
        addProperty("type", "neoforge:add_features")
        add("biomes", JsonArray().also { biomes -> biomes.add("minecraft:plains"); biomes.add("minecraft:sunflower_plains") })
        addProperty("features", "$namespace:corn_patches")
        addProperty("step", "vegetal_decoration")
    }

    override fun getName() = "SupremeMC corn"
}