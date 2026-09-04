package com.suprememc

import com.google.gson.JsonArray
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class FireCreeperDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val netherSpawnModifier = obj {
            addProperty("type", "neoforge:add_spawns")
            add("biomes", JsonArray().also { biomes ->
                biomes.add("minecraft:nether_wastes")
                biomes.add("minecraft:soul_sand_valley")
                biomes.add("minecraft:crimson_forest")
                biomes.add("minecraft:basalt_deltas")
            })
            add("spawners", obj {
                addProperty("type", "$namespace:fire_creeper")
                addProperty("weight", 25)
                addProperty("minCount", 1)
                addProperty("maxCount", 2)
            })
        }
        val model = obj {
            addProperty("parent", "minecraft:item/generated")
            add("textures", obj { addProperty("layer0", "$namespace:item/fire_creeper_spawn_egg") })
        }
        val definition = itemModelDefinition("$namespace:item/fire_creeper_spawn_egg")
        // Mirrors vanilla's data/minecraft/loot_table/entities/creeper.json (gunpowder plus
        // music discs when killed by a skeleton), under this entity's own namespace.
        val lootTable = obj {
            addProperty("type", "minecraft:entity")
            add("pools", JsonArray().also { pools ->
                pools.add(obj {
                    addProperty("rolls", 1.0)
                    add("entries", JsonArray().also { entries ->
                        entries.add(obj {
                            addProperty("type", "minecraft:item")
                            addProperty("name", "minecraft:gunpowder")
                            add("functions", JsonArray().also { functions ->
                                functions.add(obj {
                                    addProperty("function", "minecraft:set_count")
                                    add("count", obj {
                                        addProperty("type", "minecraft:uniform")
                                        addProperty("min", 0.0)
                                        addProperty("max", 2.0)
                                    })
                                })
                                functions.add(obj {
                                    addProperty("function", "minecraft:enchanted_count_increase")
                                    addProperty("enchantment", "minecraft:looting")
                                    add("count", obj {
                                        addProperty("type", "minecraft:uniform")
                                        addProperty("min", 0.0)
                                        addProperty("max", 1.0)
                                    })
                                })
                            })
                        })
                    })
                })
                pools.add(obj {
                    addProperty("rolls", 1.0)
                    add("conditions", JsonArray().also { conditions ->
                        conditions.add(obj {
                            addProperty("condition", "minecraft:entity_properties")
                            addProperty("entity", "attacker")
                            add("predicate", obj {
                                addProperty("minecraft:entity_type", "#minecraft:skeletons")
                            })
                        })
                    })
                    add("entries", JsonArray().also { entries ->
                        entries.add(obj {
                            addProperty("type", "minecraft:tag")
                            addProperty("name", "minecraft:creeper_drop_music_discs")
                            addProperty("expand", true)
                        })
                    })
                })
            })
            addProperty("random_sequence", "$namespace:entities/fire_creeper")
        }
        return CompletableFuture.allOf(
            save(cache, netherSpawnModifier, dataPath("neoforge/biome_modifier/add_fire_creepers_nether.json")),
            save(cache, model, resourcePath("models/item/fire_creeper_spawn_egg.json")),
            save(cache, definition, resourcePath("items/fire_creeper_spawn_egg.json")),
            save(cache, lootTable, dataPath("loot_table/entities/fire_creeper.json"))
        )
    }

    override fun getName() = "SupremeMC fire creeper"
}
