package com.suprememc

import com.google.gson.JsonArray
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class EnderSpiderDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val endSpawnModifier = obj {
            addProperty("type", "neoforge:add_spawns")
            add("biomes", JsonArray().also { biomes ->
                biomes.add("minecraft:the_end")
                biomes.add("minecraft:end_highlands")
                biomes.add("minecraft:end_midlands")
                biomes.add("minecraft:small_end_islands")
                biomes.add("minecraft:end_barrens")
            })
            add("spawners", obj {
                addProperty("type", "$namespace:ender_spider")
                addProperty("weight", 3)
                addProperty("minCount", 1)
                addProperty("maxCount", 4)
            })
        }
        val model = obj {
            addProperty("parent", "minecraft:item/generated")
            add("textures", obj { addProperty("layer0", "$namespace:item/ender_spider_spawn_egg") })
        }
        val definition = itemModelDefinition("$namespace:item/ender_spider_spawn_egg")
        val lootTable = obj {
            addProperty("type", "minecraft:entity")
            add("pools", JsonArray().also { pools ->
                pools.add(obj {
                    addProperty("rolls", 1.0)
                    add("entries", JsonArray().also { entries ->
                        entries.add(obj {
                            addProperty("type", "minecraft:item")
                            addProperty("name", "minecraft:string")
                            add("functions", JsonArray().also { functions ->
                                functions.add(uniformCountFunction(0.0, 2.0))
                                functions.add(lootingCountFunction(0.0, 1.0))
                            })
                        })
                    })
                })
                pools.add(obj {
                    addProperty("rolls", 1.0)
                    add("conditions", JsonArray().also { conditions ->
                        conditions.add(obj { addProperty("condition", "minecraft:killed_by_player") })
                    })
                    add("entries", JsonArray().also { entries ->
                        entries.add(obj {
                            addProperty("type", "minecraft:item")
                            addProperty("name", "minecraft:spider_eye")
                            add("functions", JsonArray().also { functions ->
                                functions.add(uniformCountFunction(-1.0, 1.0))
                                functions.add(lootingCountFunction(0.0, 1.0))
                            })
                        })
                    })
                })
                pools.add(obj {
                    addProperty("rolls", 1.0)
                    add("entries", JsonArray().also { entries ->
                        entries.add(obj {
                            addProperty("type", "minecraft:item")
                            addProperty("name", "minecraft:ender_pearl")
                            add("functions", JsonArray().also { functions ->
                                functions.add(uniformCountFunction(0.0, 1.0))
                                functions.add(lootingCountFunction(0.0, 1.0))
                            })
                        })
                    })
                })
            })
            addProperty("random_sequence", "$namespace:entities/ender_spider")
        }
        return CompletableFuture.allOf(
            save(cache, endSpawnModifier, dataPath("neoforge/biome_modifier/add_ender_spiders_end.json")),
            save(cache, model, resourcePath("models/item/ender_spider_spawn_egg.json")),
            save(cache, definition, resourcePath("items/ender_spider_spawn_egg.json")),
            save(cache, lootTable, dataPath("loot_table/entities/ender_spider.json"))
        )
    }

    private fun uniformCountFunction(min: Double, max: Double) = obj {
        addProperty("function", "minecraft:set_count")
        add("count", obj {
            addProperty("type", "minecraft:uniform")
            addProperty("min", min)
            addProperty("max", max)
        })
    }

    private fun lootingCountFunction(min: Double, max: Double) = obj {
        addProperty("function", "minecraft:enchanted_count_increase")
        addProperty("enchantment", "minecraft:looting")
        add("count", obj {
            addProperty("type", "minecraft:uniform")
            addProperty("min", min)
            addProperty("max", max)
        })
    }

    override fun getName() = "SupremeMC ender spider"
}