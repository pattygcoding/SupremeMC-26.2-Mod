package com.suprememc

import com.google.gson.JsonArray
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

/** Grape vines: hang/grow like weeping vines (see `GrapeVineBlock`/`GrapeVinePlantBlock`), always drop `grapes`. */
class GrapeVineDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        listOf("grape_vine", "grape_vine_plant").forEach { id ->
            writes += save(cache, blockState(id), resourcePath("blockstates/$id.json"))
            writes += save(cache, model("minecraft:block/cross", "cross" to "$namespace:block/$id"), resourcePath("models/block/$id.json"))
            writes += save(cache, grapeLoot(), dataPath("loot_table/blocks/$id.json"))
        }
        // Datagen-only: neither block has its own BlockItem (grapes places grape_vine directly), but
        // assets/<ns>/items/<id>.json must still resolve for every generated blockstate.
        writes += save(cache, itemModelDefinition("$namespace:block/grape_vine"), resourcePath("items/grape_vine.json"))
        writes += save(cache, itemModelDefinition("$namespace:block/grape_vine_plant"), resourcePath("items/grape_vine_plant.json"))
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun blockState(id: String) = obj {
        add("variants", obj { add("", obj { addProperty("model", "$namespace:block/$id") }) })
    }

    private fun model(parent: String, vararg textures: Pair<String, String>) = obj {
        addProperty("parent", parent)
        add("textures", obj { textures.forEach { (key, value) -> addProperty(key, value) } })
    }

    private fun grapeLoot() = obj {
        addProperty("type", "minecraft:block")
        add("pools", JsonArray().also { pools -> pools.add(obj {
            addProperty("rolls", 1)
            add("entries", JsonArray().also { entries -> entries.add(itemLootEntry("grapes")) })
        }) })
        addProperty("random_sequence", "$namespace:blocks/grape_vine")
    }

    override fun getName() = "SupremeMC grape vine"
}

