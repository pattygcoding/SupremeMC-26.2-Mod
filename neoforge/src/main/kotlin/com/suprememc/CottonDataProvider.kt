package com.suprememc

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class CottonDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        writes += save(cache, cottonBlockState(), resourcePath("blockstates/cotton_bush.json"))
        (0..3).forEach { age ->
            writes += save(cache, model("minecraft:block/cross", "cross" to "$namespace:block/cotton_bush_stage$age"), resourcePath("models/block/cotton_bush_stage$age.json"))
        }
        writes += save(cache, flatItemModel("$namespace:item/cotton"), resourcePath("models/item/cotton.json"))
        writes += save(cache, itemModelDefinition("$namespace:item/cotton"), resourcePath("items/cotton.json"))
        writes += save(cache, cottonLoot(), dataPath("loot_table/blocks/cotton_bush.json"))
        writes += save(cache, cottonLoot(), dataPath("loot_table/harvest/cotton_bush.json"))
        writes += save(cache, cottonPatch(), dataPath("worldgen/configured_feature/cotton_bushes.json"))
        writes += save(cache, cottonPlacement(), dataPath("worldgen/placed_feature/cotton_bushes.json"))
        writes += save(cache, plainsBiomeModifier(), dataPath("neoforge/biome_modifier/add_cotton_bushes.json"))
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun cottonBlockState() = variants {
        (0..3).forEach { age -> add("age=$age", variant("cotton_bush_stage$age")) }
    }

    private fun model(parent: String, vararg textures: Pair<String, String>) = obj {
        addProperty("parent", parent)
        add("textures", obj { textures.forEach { (key, value) -> addProperty(key, value) } })
    }

    private fun flatItemModel(texture: String) = model("minecraft:item/generated", "layer0" to texture)

    private fun variants(block: JsonObject.() -> Unit) = obj { add("variants", obj(block)) }

    private fun variant(blockModel: String) = obj {
        addProperty("model", "$namespace:block/$blockModel")
    }

    private fun cottonLoot() = obj {
        addProperty("type", "minecraft:block")
        add("pools", JsonArray().also { pools ->
            pools.add(cottonLootPool("2", 1, 2))
            pools.add(cottonLootPool("3", 2, 3))
        })
        addProperty("random_sequence", "$namespace:blocks/cotton_bush")
    }

    private fun cottonLootPool(age: String, minimum: Int, maximum: Int) = obj {
        addProperty("rolls", 1.0)
        add("conditions", JsonArray().also { conditions ->
            conditions.add(obj {
                addProperty("condition", "minecraft:block_state_property")
                addProperty("block", "$namespace:cotton_bush")
                add("properties", obj { addProperty("age", age) })
            })
        })
        add("entries", JsonArray().also { entries ->
            entries.add(itemLootEntry("cotton").also { entry ->
                entry.add("functions", JsonArray().also { functions ->
                    functions.add(obj { addProperty("function", "minecraft:set_count"); add("count", obj { addProperty("type", "minecraft:uniform"); addProperty("min", minimum); addProperty("max", maximum) }) })
                    functions.add(obj { addProperty("function", "minecraft:apply_bonus"); addProperty("enchantment", "minecraft:fortune"); addProperty("formula", "minecraft:uniform_bonus_count"); add("parameters", obj { addProperty("bonusMultiplier", 1) }) })
                    functions.add(obj { addProperty("function", "minecraft:explosion_decay") })
                })
            })
        })
    }

    private fun cottonPatch() = obj {
        addProperty("type", "minecraft:random_patch")
        add("config", obj {
            addProperty("tries", 32)
            addProperty("xz_spread", 4)
            addProperty("y_spread", 1)
            add("feature", obj {
                addProperty("feature", "minecraft:simple_block")
                add("config", obj {
                    add("to_place", obj {
                        addProperty("type", "minecraft:simple_state_provider")
                        add("state", obj { addProperty("Name", "$namespace:cotton_bush") })
                    })
                })
            })
        })
    }

    private fun cottonPlacement() = obj {
        addProperty("feature", "$namespace:cotton_bushes")
        add("placement", JsonArray().also { placement ->
            placement.add(obj { addProperty("type", "minecraft:rarity_filter"); addProperty("chance", 8) })
            placement.add(obj { addProperty("type", "minecraft:in_square") })
            placement.add(obj { addProperty("type", "minecraft:heightmap"); addProperty("heightmap", "MOTION_BLOCKING") })
            placement.add(obj { addProperty("type", "minecraft:biome") })
        })
    }

    private fun plainsBiomeModifier() = obj {
        addProperty("type", "neoforge:add_features")
        add("biomes", JsonArray().also { biomes -> biomes.add("minecraft:plains"); biomes.add("minecraft:sunflower_plains") })
        addProperty("features", "$namespace:cotton_bushes")
        addProperty("step", "vegetal_decoration")
    }

    override fun getName() = "SupremeMC cotton"
}