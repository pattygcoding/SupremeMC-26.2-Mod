package com.suprememc

import com.google.gson.JsonArray
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class XyliumDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        listOf("xylium_ore", "xylium_block").forEach { id ->
            writes += save(cache, blockState(id), resourcePath("blockstates/$id.json"))
            writes += save(cache, cubeModel(id), resourcePath("models/block/$id.json"))
            writes += save(cache, itemModelDefinition("$namespace:block/$id"), resourcePath("items/$id.json"))
            writes += save(cache, obj { addProperty("parent", "$namespace:block/$id") }, resourcePath("models/item/$id.json"))
        }
        writes += save(cache, xyliumOreLoot(), dataPath("loot_table/blocks/xylium_ore.json"))
        writes += save(cache, selfDropLootTable("xylium_block"), dataPath("loot_table/blocks/xylium_block.json"))
        writes += save(cache, oreFeature(), dataPath("worldgen/configured_feature/xylium_ore.json"))
        writes += save(cache, placedFeature(), dataPath("worldgen/placed_feature/xylium_ore.json"))
        writes += save(cache, obj { addProperty("type", "neoforge:add_features"); add("biomes", array("#minecraft:is_end")); addProperty("features", "$namespace:xylium_ore"); addProperty("step", "underground_ores") }, dataPath("neoforge/biome_modifier/add_xylium_ore.json"))
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun blockState(id: String) = obj { add("variants", obj { add("", obj { addProperty("model", "$namespace:block/$id") }) }) }
    private fun cubeModel(id: String) = obj { addProperty("parent", "minecraft:block/cube_all"); add("textures", obj { addProperty("all", "$namespace:block/$id") }) }

    private fun xyliumOreLoot() = obj {
        addProperty("type", "minecraft:block")
        add("pools", JsonArray().also { pools ->
            pools.add(lootPool(itemLootEntry("xylium_ore"), silkTouchCondition()))
            pools.add(lootPool(itemLootEntry("xylium_dust"), nonSilkTouchCondition()).apply {
                getAsJsonArray("entries").single().asJsonObject.add("functions", JsonArray().also { functions ->
                    functions.add(obj { addProperty("function", "minecraft:set_count"); add("count", obj { addProperty("type", "minecraft:uniform"); addProperty("min", 4); addProperty("max", 5) }) })
                    functions.add(obj { addProperty("function", "minecraft:apply_bonus"); addProperty("enchantment", "minecraft:fortune"); addProperty("formula", "minecraft:ore_drops") })
                    functions.add(obj { addProperty("function", "minecraft:explosion_decay") })
                })
            })
        })
    }

    private fun lootPool(entry: com.google.gson.JsonObject, condition: com.google.gson.JsonObject) = obj {
        addProperty("rolls", 1)
        add("entries", JsonArray().also { it.add(entry.apply { add("conditions", JsonArray().also { it.add(condition) }) }) })
    }

    private fun silkTouchCondition() = obj {
        addProperty("condition", "minecraft:match_tool")
        add("predicate", obj { add("predicates", obj { add("minecraft:enchantments", JsonArray().also { it.add(obj { addProperty("enchantments", "minecraft:silk_touch"); add("levels", obj { addProperty("min", 1) }) }) }) }) })
    }

    private fun nonSilkTouchCondition() = obj { addProperty("condition", "minecraft:inverted"); add("term", silkTouchCondition()) }

    private fun oreFeature() = obj {
        addProperty("type", "minecraft:ore")
        add("config", obj {
            addProperty("discard_chance_on_air_exposure", 0.0f)
            addProperty("size", 17)
            add("targets", JsonArray().also { it.add(oreTarget()) })
        })
    }

    private fun oreTarget() = obj {
        add("state", obj { addProperty("Name", "$namespace:xylium_ore") })
        add("target", obj { addProperty("predicate_type", "minecraft:block_match"); addProperty("block", "minecraft:end_stone") })
    }

    private fun placedFeature() = obj {
        addProperty("feature", "$namespace:xylium_ore")
        add("placement", JsonArray().also {
            it.add(obj { addProperty("type", "minecraft:count"); addProperty("count", 24) })
            it.add(obj { addProperty("type", "minecraft:in_square") })
            it.add(obj {
                addProperty("type", "minecraft:height_range")
                add("height", obj {
                    addProperty("type", "minecraft:uniform")
                    add("min_inclusive", obj { addProperty("above_bottom", 10) })
                    add("max_inclusive", obj { addProperty("below_top", 10) })
                })
            })
            it.add(obj { addProperty("type", "minecraft:biome") })
        })
    }

    override fun getName() = "SupremeMC xylium resources"
}