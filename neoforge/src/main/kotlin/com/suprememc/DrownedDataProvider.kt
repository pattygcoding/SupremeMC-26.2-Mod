package com.suprememc

import com.google.gson.JsonArray
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class DrownedDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    override fun getName() = "Drowned spawn data"

    private val targetBiomes = array(
        "#minecraft:is_ocean",
        "minecraft:river",
        "minecraft:dripstone_caves",
        "minecraft:frozen_river",
        "$namespace:ice_caves"
    )

    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        writes += save(cache, removeDrowned(), dataPath("neoforge/biome_modifier/remove_vanilla_drowned_spawns.json"))
        writes += save(cache, addDrowned("river", 100, 1, 1), dataPath("neoforge/biome_modifier/add_drowned_river.json"))
        writes += save(cache, addDrowned("dripstone_caves", 100, 4, 4), dataPath("neoforge/biome_modifier/add_drowned_dripstone_caves.json"))
        writes += save(cache, addDrowned("ice_caves", 100, 4, 4), dataPath("neoforge/biome_modifier/add_drowned_ice_caves.json"))
        writes += save(cache, addDrowned("oceans", 100, 1, 1), dataPath("neoforge/biome_modifier/add_drowned_oceans.json"))
        writes += save(cache, addDrowned("frozen_river", 5, 1, 1), dataPath("neoforge/biome_modifier/add_drowned_frozen_river.json"))
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun removeDrowned() = obj {
        addProperty("type", "neoforge:remove_spawns")
        add("biomes", targetBiomes.deepCopy())
        addProperty("entity_types", "minecraft:drowned")
    }

    private fun addDrowned(group: String, weight: Int, minCount: Int, maxCount: Int) = obj {
        addProperty("type", "neoforge:add_spawns")
        add("biomes", when (group) {
            "oceans" -> JsonArray().also { it.add("#minecraft:is_ocean") }
            "river" -> JsonArray().also { it.add("minecraft:river") }
            "dripstone_caves" -> JsonArray().also { it.add("minecraft:dripstone_caves") }
            "ice_caves" -> JsonArray().also { it.add("$namespace:ice_caves") }
            else -> JsonArray().also { it.add("minecraft:frozen_river") }
        })
        add("spawners", obj {
            addProperty("type", "minecraft:drowned")
            addProperty("weight", weight)
            addProperty("minCount", minCount)
            addProperty("maxCount", maxCount)
        })
    }
}