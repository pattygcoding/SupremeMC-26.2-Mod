package com.suprememc

import com.google.gson.JsonArray
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class PrismarineDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    private val ores = arrayOf("prismarine_ore", "deepslate_prismarine_ore")

    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        ores.forEach { id ->
            writes += save(cache, blockState(id), resourcePath("blockstates/$id.json"))
            writes += save(cache, cubeModel(id), resourcePath("models/block/$id.json"))
            writes += save(cache, itemModelDefinition("$namespace:block/$id"), resourcePath("items/$id.json"))
            writes += save(cache, obj { addProperty("parent", "$namespace:block/$id") }, resourcePath("models/item/$id.json"))
            writes += save(cache, oreLoot(id), dataPath("loot_table/blocks/$id.json"))
        }
        writes += save(cache, configuredFeature(), dataPath("worldgen/configured_feature/prismarine_ore.json"))
        writes += save(cache, placedFeature(), dataPath("worldgen/placed_feature/prismarine_ore.json"))
        writes += save(cache, obj {
            addProperty("type", "neoforge:add_features")
            add("biomes", array("#minecraft:is_ocean"))
            addProperty("features", "$namespace:prismarine_ore")
            addProperty("step", "underground_ores")
        }, dataPath("neoforge/biome_modifier/add_prismarine_ore.json"))
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun blockState(id: String) = obj { add("variants", obj { add("", obj { addProperty("model", "$namespace:block/$id") }) }) }
    private fun cubeModel(id: String) = obj { addProperty("parent", "minecraft:block/cube_all"); add("textures", obj { addProperty("all", "$namespace:block/$id") }) }

    private fun oreLoot(id: String) = obj {
        addProperty("type", "minecraft:block")
        add("pools", JsonArray().also { pools ->
            pools.add(lootPool(itemLootEntry(id), silkTouchCondition()))
            pools.add(lootPool(minecraftItemLootEntry("prismarine_crystals"), nonSilkTouchCondition()).apply {
                getAsJsonArray("entries").single().asJsonObject.add("functions", fortuneFunctions())
            })
            pools.add(lootPool(minecraftItemLootEntry("prismarine_shard"), nonSilkTouchCondition()).apply {
                getAsJsonArray("entries").single().asJsonObject.add("functions", JsonArray().also { functions ->
                    functions.add(obj { addProperty("function", "minecraft:set_count"); add("count", obj { addProperty("type", "minecraft:uniform"); addProperty("min", 2); addProperty("max", 3) }) })
                    functions.add(obj { addProperty("function", "minecraft:apply_bonus"); addProperty("enchantment", "minecraft:fortune"); addProperty("formula", "minecraft:ore_drops") })
                    functions.add(obj { addProperty("function", "minecraft:explosion_decay") })
                })
            })
        })
    }

    private fun lootPool(entry: com.google.gson.JsonObject, condition: com.google.gson.JsonObject) = obj {
        addProperty("rolls", 1)
        add("entries", JsonArray().also { it.add(entry.apply { add("conditions", JsonArray().also { conditions -> conditions.add(condition) }) }) })
    }

    private fun fortuneFunctions() = JsonArray().also { functions ->
        functions.add(obj { addProperty("function", "minecraft:apply_bonus"); addProperty("enchantment", "minecraft:fortune"); addProperty("formula", "minecraft:ore_drops") })
        functions.add(obj { addProperty("function", "minecraft:explosion_decay") })
    }

    private fun minecraftItemLootEntry(id: String) = obj {
        addProperty("type", "minecraft:item")
        addProperty("name", "minecraft:$id")
    }

    private fun silkTouchCondition() = obj {
        addProperty("condition", "minecraft:match_tool")
        add("predicate", obj { add("predicates", obj { add("minecraft:enchantments", JsonArray().also { it.add(obj { addProperty("enchantments", "minecraft:silk_touch"); add("levels", obj { addProperty("min", 1) }) }) }) }) })
    }

    private fun nonSilkTouchCondition() = obj {
        addProperty("condition", "minecraft:inverted")
        add("term", silkTouchCondition())
    }

    private fun configuredFeature() = obj {
        addProperty("type", "minecraft:ore")
        add("config", obj {
            addProperty("discard_chance_on_air_exposure", 0.0f)
            addProperty("size", 4)
            add("targets", JsonArray().also {
                it.add(oreTarget("$namespace:prismarine_ore", "minecraft:stone_ore_replaceables"))
                it.add(oreTarget("$namespace:deepslate_prismarine_ore", "minecraft:deepslate_ore_replaceables"))
            })
        })
    }

    private fun oreTarget(block: String, tag: String) = obj { add("state", obj { addProperty("Name", block) }); add("target", obj { addProperty("predicate_type", "minecraft:tag_match"); addProperty("tag", tag) }) }
    private fun placedFeature() = obj { addProperty("feature", "$namespace:prismarine_ore"); add("placement", JsonArray().also { it.add(obj { addProperty("type", "minecraft:count"); addProperty("count", 7) }); it.add(obj { addProperty("type", "minecraft:in_square") }); it.add(obj { addProperty("type", "minecraft:height_range"); add("height", obj { addProperty("type", "minecraft:trapezoid"); add("min_inclusive", obj { addProperty("above_bottom", -32) }); add("max_inclusive", obj { addProperty("above_bottom", 32) }) }) }); it.add(obj { addProperty("type", "minecraft:biome") }) }) }
    override fun getName() = "SupremeMC prismarine ore resources"
}
