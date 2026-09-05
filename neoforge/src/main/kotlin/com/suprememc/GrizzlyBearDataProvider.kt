package com.suprememc

import com.google.gson.JsonArray
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class GrizzlyBearDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    // Land-only cold biomes outside the taiga family (oceans/rivers are left to polar bears).
    private val otherColdBiomes = array(
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
        "minecraft:stony_peaks"
    )

    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val model = obj {
            addProperty("parent", "minecraft:item/generated")
            add("textures", obj { addProperty("layer0", "$namespace:item/grizzly_bear_spawn_egg") })
        }
        val definition = itemModelDefinition("$namespace:item/grizzly_bear_spawn_egg")
        return CompletableFuture.allOf(
            save(cache, model, resourcePath("models/item/grizzly_bear_spawn_egg.json")),
            save(cache, definition, resourcePath("items/grizzly_bear_spawn_egg.json")),
            save(cache, addGrizzlyBear(array("#minecraft:is_taiga"), 8),
                dataPath("neoforge/biome_modifier/add_grizzly_bear_taiga.json")),
            save(cache, addGrizzlyBear(otherColdBiomes, 8),
                dataPath("neoforge/biome_modifier/add_grizzly_bear_cold.json"))
        )
    }

    private fun addGrizzlyBear(biomes: JsonArray, weight: Int) = obj {
        addProperty("type", "neoforge:add_spawns")
        add("biomes", biomes)
        add("spawners", obj {
            addProperty("type", "$namespace:grizzly_bear")
            addProperty("weight", weight)
            addProperty("minCount", 1)
            addProperty("maxCount", 2)
        })
    }

    override fun getName() = "SupremeMC grizzly bear"
}