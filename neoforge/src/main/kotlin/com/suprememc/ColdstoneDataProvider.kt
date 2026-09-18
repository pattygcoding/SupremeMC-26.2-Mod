package com.suprememc

import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class ColdstoneDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val id = "coldstone"
        val writes = listOf(
            save(cache, simpleBlockState(id), resourcePath("blockstates/$id.json")),
            save(cache, cubeModel(id), resourcePath("models/block/$id.json")),
            save(cache, itemModelDefinition("$namespace:block/$id"), resourcePath("items/$id.json")),
            save(cache, selfDropLootTable(id), dataPath("loot_table/blocks/$id.json"))
        )
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    override fun getName() = "SupremeMC coldstone"

    private fun simpleBlockState(id: String) = obj {
        add("variants", obj { add("", variant(id)) })
    }

    private fun variant(id: String) = obj {
        addProperty("model", "$namespace:block/$id")
    }

    private fun cubeModel(id: String) = obj {
        addProperty("parent", "minecraft:block/cube_all")
        add("textures", obj { addProperty("all", "$namespace:block/$id") })
    }
}