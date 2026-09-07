package com.suprememc

import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class DustDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        listOf("experience_dust", "xylium_dust").forEach { id ->
            writes += save(cache, itemModel(id), resourcePath("models/item/$id.json"))
            writes += save(cache, itemModelDefinition("$namespace:item/$id"), resourcePath("items/$id.json"))
        }
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun itemModel(id: String) = obj {
        addProperty("parent", "minecraft:item/generated")
        add("textures", obj { addProperty("layer0", "$namespace:item/$id") })
    }

    override fun getName() = "SupremeMC dust resources"
}
