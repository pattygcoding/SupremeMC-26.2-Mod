package com.suprememc

import com.google.gson.JsonArray
import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class CornDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        listOf("corn_stalk", "corn_stalk_plant").forEach { id ->
            writes += save(cache, blockState(id), resourcePath("blockstates/$id.json"))
            writes += save(cache, model("minecraft:block/cross", "cross" to "$namespace:block/$id"), resourcePath("models/block/$id.json"))
            writes += save(cache, cornLoot(), dataPath("loot_table/blocks/$id.json"))
        }
        listOf("corn_stalk", "corn_stalk_plant").forEach { id ->
            writes += save(cache, itemModelDefinition("$namespace:block/$id"), resourcePath("items/$id.json"))
        }
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun blockState(id: String) = obj {
        add("variants", obj { add("", obj { addProperty("model", "$namespace:block/$id") }) })
    }

    private fun model(parent: String, vararg textures: Pair<String, String>) = obj {
        addProperty("parent", parent)
        add("textures", obj { textures.forEach { (key, value) -> addProperty(key, value) } })
    }

    private fun cornLoot() = obj {
        addProperty("type", "minecraft:block")
        add("pools", JsonArray().also { pools -> pools.add(obj {
            addProperty("rolls", 1)
            add("entries", JsonArray().also { entries -> entries.add(itemLootEntry("corn")) })
        }) })
        addProperty("random_sequence", "$namespace:blocks/corn_stalk")
    }

    override fun getName() = "SupremeMC corn"
}