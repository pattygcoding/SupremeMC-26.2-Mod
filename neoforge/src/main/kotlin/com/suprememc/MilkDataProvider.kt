package com.suprememc

import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class MilkDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        writes += save(cache, fluidBlockState("milk"), resourcePath("blockstates/milk.json"))
        writes += save(cache, fluidModel(), resourcePath("models/block/milk.json"))
        writes += save(cache, milkCauldronBlockState(), resourcePath("blockstates/milk_cauldron.json"))
        writes += save(cache, valuesTag("$namespace:milk", "$namespace:flowing_milk"), minecraftDataPath("tags/fluid/water.json"))
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun fluidBlockState(id: String) = obj {
        add("variants", obj { add("", obj { addProperty("model", "$namespace:block/$id") }) })
    }

    private fun fluidModel() = obj {
        add("textures", obj { addProperty("particle", "$namespace:block/milk_still") })
    }

    private fun milkCauldronBlockState() = obj {
        add("variants", obj {
            add("level=1", obj { addProperty("model", "minecraft:block/cauldron") })
            add("level=2", obj { addProperty("model", "minecraft:block/cauldron") })
            add("level=3", obj { addProperty("model", "minecraft:block/water_cauldron") })
        })
    }

    override fun getName() = "SupremeMC milk"
}