package com.suprememc

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class TomatoDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        writes += save(cache, tomatoBlockState(), resourcePath("blockstates/tomato_bush.json"))
        (0..3).forEach { age ->
            writes += save(cache, model("minecraft:block/cross", "cross" to "$namespace:block/tomato_bush_stage$age"), resourcePath("models/block/tomato_bush_stage$age.json"))
        }
        writes += save(cache, itemModelDefinition("$namespace:item/tomato"), resourcePath("items/tomato.json"))
        writes += save(cache, itemModelDefinition("$namespace:block/tomato_bush_stage3"), resourcePath("items/tomato_bush.json"))
        writes += save(cache, tomatoLoot(), dataPath("loot_table/blocks/tomato_bush.json"))
        writes += save(cache, tomatoLoot(), dataPath("loot_table/harvest/tomato_bush.json"))
        writes += save(cache, tomatoPatch(), dataPath("worldgen/configured_feature/tomato_bushes.json"))
        writes += save(cache, tomatoPlacement(), dataPath("worldgen/placed_feature/tomato_bushes.json"))
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun tomatoBlockState() = variants {
        (0..3).forEach { age -> add("age=$age", variant("tomato_bush_stage$age")) }
    }

    private fun model(parent: String, vararg textures: Pair<String, String>) = obj {
        addProperty("parent", parent)
        add("textures", obj { textures.forEach { (key, value) -> addProperty(key, value) } })
    }

    private fun variants(block: JsonObject.() -> Unit) = obj { add("variants", obj(block)) }

    private fun variant(blockModel: String) = obj {
        addProperty("model", "$namespace:block/$blockModel")
    }

    private fun tomatoLoot() = obj {
        addProperty("type", "minecraft:block")
        add("pools", JsonArray().also { pools ->
            pools.add(tomatoLootPool("2", 1, 2))
            pools.add(tomatoLootPool("3", 2, 3))
        })
        addProperty("random_sequence", "$namespace:blocks/tomato_bush")
    }

    private fun tomatoLootPool(age: String, minimum: Int, maximum: Int) = obj {
        addProperty("rolls", 1.0)
        add("conditions", JsonArray().also { conditions ->
            conditions.add(obj {
                addProperty("condition", "minecraft:block_state_property")
                addProperty("block", "$namespace:tomato_bush")
                add("properties", obj { addProperty("age", age) })
            })
        })
        add("entries", JsonArray().also { entries ->
            entries.add(itemLootEntry("tomato").also { entry ->
                entry.add("functions", JsonArray().also { functions ->
                    functions.add(obj { addProperty("function", "minecraft:set_count"); add("count", obj { addProperty("type", "minecraft:uniform"); addProperty("min", minimum); addProperty("max", maximum) }) })
                    functions.add(obj { addProperty("function", "minecraft:apply_bonus"); addProperty("enchantment", "minecraft:fortune"); addProperty("formula", "minecraft:uniform_bonus_count"); add("parameters", obj { addProperty("bonusMultiplier", 1) }) })
                    functions.add(obj { addProperty("function", "minecraft:explosion_decay") })
                })
            })
        })
    }

    private fun tomatoPatch() = obj {
        addProperty("type", "minecraft:simple_block")
        add("config", obj {
            add("to_place", obj {
                addProperty("type", "minecraft:simple_state_provider")
                add("state", obj {
                    addProperty("Name", "$namespace:tomato_bush")
                    add("Properties", obj { addProperty("age", "3") })
                })
            })
        })
    }

    private fun tomatoPlacement() = obj {
        addProperty("feature", "$namespace:tomato_bushes")
        add("placement", JsonArray().also { placement ->
            placement.add(obj { addProperty("type", "minecraft:rarity_filter"); addProperty("chance", 4) })
            placement.add(obj { addProperty("type", "minecraft:in_square") })
            placement.add(obj { addProperty("type", "minecraft:heightmap"); addProperty("heightmap", "MOTION_BLOCKING") })
            placement.add(obj { addProperty("type", "minecraft:biome") })
            placement.add(obj { addProperty("type", "minecraft:count"); addProperty("count", 32) })
            placement.add(obj {
                addProperty("type", "minecraft:random_offset")
                add("xz_spread", trapezoid(4))
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

    override fun getName() = "SupremeMC tomato"
}