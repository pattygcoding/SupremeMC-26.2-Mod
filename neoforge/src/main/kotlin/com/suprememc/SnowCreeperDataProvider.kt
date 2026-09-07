package com.suprememc

import com.google.gson.JsonArray
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class SnowCreeperDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    private val coldBiomes = array(
        "minecraft:snowy_plains",
        "minecraft:ice_spikes",
        "minecraft:grove",
        "minecraft:snowy_slopes",
        "minecraft:frozen_peaks",
        "minecraft:jagged_peaks",
        "minecraft:snowy_beach",
        "minecraft:windswept_hills",
        "minecraft:windswept_forest",
        "minecraft:windswept_gravelly_hills",
        "minecraft:stony_peaks",
        "minecraft:taiga",
        "minecraft:snowy_taiga",
        "minecraft:old_growth_pine_taiga",
        "minecraft:old_growth_spruce_taiga"
    )

    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val model = obj {
            addProperty("parent", "minecraft:item/generated")
            add("textures", obj { addProperty("layer0", "$namespace:item/snow_creeper_spawn_egg") })
        }
        val definition = itemModelDefinition("$namespace:item/snow_creeper_spawn_egg")
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
                                functions.add(uniformCountFunction(0.0, 2.0))
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
                            addProperty("name", "minecraft:snowball")
                            add("functions", JsonArray().also { functions ->
                                functions.add(uniformCountFunction(4.0, 8.0))
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
            addProperty("random_sequence", "$namespace:entities/snow_creeper")
        }
        return CompletableFuture.allOf(
            save(cache, model, resourcePath("models/item/snow_creeper_spawn_egg.json")),
            save(cache, definition, resourcePath("items/snow_creeper_spawn_egg.json")),
            save(cache, lootTable, dataPath("loot_table/entities/snow_creeper.json")),
            save(cache, removeVanillaCreepers(), dataPath("neoforge/biome_modifier/remove_vanilla_creepers_cold.json")),
            save(cache, addSnowCreepers(), dataPath("neoforge/biome_modifier/add_snow_creepers_cold.json"))
        )
    }

    private fun removeVanillaCreepers() = obj {
        addProperty("type", "neoforge:remove_spawns")
        add("biomes", coldBiomes.deepCopy())
        addProperty("entity_types", "minecraft:creeper")
    }

    private fun addSnowCreepers() = obj {
        addProperty("type", "neoforge:add_spawns")
        add("biomes", coldBiomes.deepCopy())
        add("spawners", obj {
            addProperty("type", "$namespace:snow_creeper")
            addProperty("weight", 100)
            addProperty("minCount", 4)
            addProperty("maxCount", 4)
        })
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

    override fun getName() = "SupremeMC snow creeper"
}