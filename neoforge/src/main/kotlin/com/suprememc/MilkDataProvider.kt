package com.suprememc

import net.minecraft.data.CachedOutput
import net.minecraft.data.PackOutput
import java.util.concurrent.CompletableFuture

class MilkDataProvider(output: PackOutput) : EcosystemDataProvider(output) {
    override fun run(cache: CachedOutput): CompletableFuture<*> {
        val writes = mutableListOf<CompletableFuture<*>>()
        writes += save(cache, fluidBlockState("milk"), resourcePath("blockstates/milk.json"))
        writes += save(cache, fluidModel(), resourcePath("models/block/milk.json"))
        writes += save(cache, itemModelDefinition("$namespace:block/milk"), resourcePath("items/milk.json"))
        writes += save(cache, milkCauldronBlockState(), resourcePath("blockstates/milk_cauldron.json"))
        writes += save(cache, obj { addProperty("parent", "minecraft:block/cauldron") }, resourcePath("models/block/milk_cauldron.json"))
        for (level in 1..3) {
            writes += save(cache, milkCauldronModel(level), resourcePath("models/block/milk_cauldron_level$level.json"))
        }
        writes += save(cache, itemModelDefinition("$namespace:block/milk_cauldron"), resourcePath("items/milk_cauldron.json"))
        writes += save(cache, flatItemModel("$namespace:item/milk_bottle"), resourcePath("models/item/milk_bottle.json"))
        writes += save(cache, itemModelDefinition("$namespace:item/milk_bottle"), resourcePath("items/milk_bottle.json"))
        writes += save(cache, valuesTag("$namespace:milk", "$namespace:flowing_milk"), minecraftDataPath("tags/fluid/water.json"))
        return CompletableFuture.allOf(*writes.toTypedArray())
    }

    private fun fluidBlockState(id: String) = obj {
        add("variants", obj { add("", obj { addProperty("model", "$namespace:block/$id") }) })
    }

    private fun fluidModel() = obj {
        add("textures", obj { addProperty("particle", "$namespace:block/milk_still") })
    }

    private fun flatItemModel(texture: String) = obj {
        addProperty("parent", "minecraft:item/generated")
        add("textures", obj { addProperty("layer0", texture) })
    }

    private fun milkCauldronBlockState() = obj {
        add("variants", obj {
            for (level in 1..3) {
                add("level=$level", obj { addProperty("model", "$namespace:block/milk_cauldron_level$level") })
            }
        })
    }

    private fun milkCauldronModel(level: Int) = obj {
        addProperty("parent", "minecraft:block/template_cauldron_${if (level == 3) "full" else "level$level"}")
        add("textures", obj {
            addProperty("content", "$namespace:block/milk_still")
        })
    }

    override fun getName() = "SupremeMC milk"
}